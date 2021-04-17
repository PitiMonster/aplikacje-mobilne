package com.example.todoapp.ui.addedittask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentAddEditTaskBinding
import com.example.todoapp.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.lang.reflect.Method
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {

    private val viewModel: AddEditTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditTaskBinding.bind(view)

        binding.apply {
            editTextTaskName.setText(viewModel.taskName)
            checkBoxImportant.isChecked = viewModel.taskImportance
            checkBoxImportant.jumpDrawablesToCurrentState()
            textViewDateCreated.isVisible = viewModel.task != null
            textViewDateCreated.text = "Created: ${viewModel.task?.createDateFormatted}"
            when (viewModel.taskExecTime) {
                0.toLong() -> textViewExecTime.text = "Date not picked yet"
                else -> textViewExecTime.text = "To: ${
                    SimpleDateFormat(
                        "dd/MM HH:mm",
                        Locale.ENGLISH
                    ).format(viewModel.taskExecTime)
                }"
            }
//            textViewExecTime.text = "To: ${SimpleDateFormat("dd/MM HH:mm",Locale.ENGLISH).format(viewModel.taskExecTime)}"
            labelTaskType.isVisible = true
            labelTaskType.text = "Select task type"

            when (viewModel.task?.categoryNumber) {
                1 -> labelCategory.setBackgroundResource(R.drawable.home)
                2 -> labelCategory.setBackgroundResource(R.drawable.work)
                3 -> labelCategory.setBackgroundResource(R.drawable.school)
            }

            val popup = PopupMenu(requireContext(), labelCategory)

            popup.apply {
                // inflate the popup menu
                menuInflater.inflate(R.menu.menu_icon_chooser, menu)

                // popup menu item click listener
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.home_task -> {
                            labelCategory.setBackgroundResource(R.drawable.home)
                            viewModel.taskCatNumber = 1
                            true
                        }
                        R.id.work_task -> {
                            labelCategory.setBackgroundResource(R.drawable.work)
                            viewModel.taskCatNumber = 2
                            true
                        }
                        R.id.school_task -> {
                            labelCategory.setBackgroundResource(R.drawable.school)
                            viewModel.taskCatNumber = 3
                            true

                        }
                    }

                    false
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    popup.setForceShowIcon(true)
                } else {
                    try {
                        val fields = popup.javaClass.declaredFields
                        for (field in fields) {
                            if ("mPopup" == field.name) {
                                field.isAccessible = true
                                val menuPopupHelper = field[popup]
                                val classPopupHelper =
                                    Class.forName(menuPopupHelper.javaClass.name)
                                val setForceIcons: Method = classPopupHelper.getMethod(
                                    "setForceShowIcon",
                                    Boolean::class.javaPrimitiveType
                                )
                                setForceIcons.invoke(menuPopupHelper, true)
                                break
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                labelCategory.setOnClickListener {
                    showPopupMenu(popup)
                }

                val cal = Calendar.getInstance()
                cal.timeInMillis = viewModel.task?.execTime ?: Calendar.getInstance().timeInMillis
                binding.apply {

                    btnPickDate.setOnClickListener {
                        val timeSetListener =
                            DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                                cal.set(Calendar.DAY_OF_MONTH, day)
                                cal.set(Calendar.YEAR, year)
                                cal.set(Calendar.MONTH, month)
                                textViewExecTime.text =
                                    SimpleDateFormat(
                                        "dd/MM HH:mm",
                                        Locale.ENGLISH
                                    ).format(cal.timeInMillis)
                                viewModel.taskExecTime = cal.timeInMillis
                            }
                        DatePickerDialog(
                            requireContext(),
                            timeSetListener,
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }

                    btnPickTime.setOnClickListener {
                        val timeSetListener =
                            TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                                cal.set(Calendar.HOUR_OF_DAY, hour)
                                cal.set(Calendar.MINUTE, minute)
                                textViewExecTime.text =
                                    SimpleDateFormat(
                                        "dd/MM HH:mm",
                                        Locale.ENGLISH
                                    ).format(cal.timeInMillis)
                                viewModel.taskExecTime = cal.timeInMillis
                            }
                        TimePickerDialog(
                            requireContext(),
                            timeSetListener,
                            cal.get(Calendar.HOUR_OF_DAY),
                            cal.get(Calendar.MINUTE),
                            true
                        ).show()
                    }

                }



                editTextTaskName.addTextChangedListener {
                    viewModel.taskName = it.toString()
                }

                checkBoxImportant.setOnCheckedChangeListener { _, isChecked ->
                    viewModel.taskImportance = isChecked
                }

                fabSaveTask.setOnClickListener {
                    viewModel.onSaveClick()
                }
            }

            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                viewModel.addEditTaskEvent.collect { event ->
                    when (event) {
                        is AddEditTaskViewModel.AddEditTaskEvent.NavigateBackWithResult -> {
                            binding.editTextTaskName.clearFocus()
                            setFragmentResult(
                                "add_edit_request",
                                bundleOf("add_edit_result" to event.result)
                            )
                            findNavController().popBackStack()
                        }
                        is AddEditTaskViewModel.AddEditTaskEvent.ShowInvalidInputMessage -> {
                            Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                        }
                    }.exhaustive
                }
            }
        }


    }

    private fun showPopupMenu(popup: PopupMenu) {
        popup.show()
    }
}