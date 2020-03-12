package com.aakashbista.sudoku

import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_game_screen.*


class GameScreen : Fragment() {

    companion object {
        private const val GRID_SAVEINSTANCE_KEY = "sudoku-grid"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_screen, container, false)
    }


   private val map = mutableMapOf<Int, Map<Int, EditText>>()

  private  var grid = arrayOf(
//        intArrayOf(7, 8, 0, 4, 0, 0, 1, 2, 0),
//        intArrayOf(6, 0, 0, 0, 7, 5, 0, 0, 9),
//        intArrayOf(0, 0, 0, 6, 0, 1, 0, 7, 8),
//        intArrayOf(0, 0, 7, 0, 4, 0, 2, 6, 0),
//        intArrayOf(0, 0, 1, 0, 5, 0, 9, 3, 0),
//        intArrayOf(9, 0, 4, 0, 6, 0, 0, 0, 5),
//        intArrayOf(0, 7, 0, 3, 0, 0, 0, 1, 2),
//        intArrayOf(1, 2, 0, 0, 0, 7, 4, 0, 0),
//        intArrayOf(0, 4, 9, 2, 0, 6, 0, 0, 0)
        intArrayOf(8, 0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 3, 6, 0, 0, 0, 0, 0),
        intArrayOf(0, 7, 0, 0, 9, 0, 2, 0, 0),
        intArrayOf(0, 5, 0, 0, 0, 7, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 4, 5, 7, 0, 0),
        intArrayOf(0, 0, 0, 1, 0, 0, 0, 3, 0),
        intArrayOf(0, 0, 1, 0, 0, 0, 0, 6, 8),
        intArrayOf(0, 0, 8, 5, 0, 0, 0, 1, 0),
        intArrayOf(0, 9, 0, 0, 0, 0, 4, 0, 0)
    )

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(GRID_SAVEINSTANCE_KEY, grabInputs())
        super.onSaveInstanceState(outState)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        val instance: Array<IntArray>? = savedInstanceState?.getSerializable(
            GRID_SAVEINSTANCE_KEY
        ) as Array<IntArray>?

        instance?.let { setSudoku(instance) }

        super.onViewStateRestored(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for (row in 0..8) {
            val linearLayout = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
            }
            val innerMap = mutableMapOf<Int, EditText>()

            for (col in 0..8) {
                val edit = EditText(context).apply {
                    setText("")
                    filters = arrayOf<InputFilter>(LengthFilter(1))
                    gravity = Gravity.CENTER
                    val orientation = resources.configuration.orientation
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        setPadding(40.dpToPixels(resources))
                    }

                    isSingleLine = true
                    setBackgroundColor(Color.parseColor("#e3e3e3"))
                    imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
                    layoutParams = LinearLayout.LayoutParams(
                        24.dpToPixels(resources),
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {

                        weight = 1f
                        setMargins(2, 2, 2, 2)
                        inputType = InputType.TYPE_CLASS_NUMBER
                        if ((col + 1) % 3 == 0 && col != 8) {
                            rightMargin = 50
                        }
                        if ((row + 1) % 3 == 0 && row != 8) {
                            bottomMargin = 50

                        }
                    }
                }
                innerMap[col] = edit
                linearLayout.addView(edit)
            }
            map[row] = innerMap
            content.addView(linearLayout)
        }

        solveButton.setOnClickListener {
            val initialValue = grabInputs()
            if (SodukuSolver().solve(initialValue)) {
                setSudoku(initialValue)
                message.text = getString(R.string.sudoku_success_message)
                message.setTextColor(
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        requireContext().resources.getColor(R.color.colorPrimary, null)
                    } else {
                        requireContext().resources.getColor(R.color.colorPrimary)
                    }
                )
            } else {
                message.text = getString(R.string.sudoku_error_message)
                message.setTextColor(Color.RED)
            }
        }

        buttonReset.setOnClickListener {

            val initialValue = grabInputs()
            for (row in 0..8) {
                for (col in 0..8) {
                    initialValue[row][col] = 0
                }
            }
            setSudoku(initialValue)
            message.text = ""
        }

        setSudoku(arr = grid)
    }

    private fun grabInputs(): Array<IntArray> {
        val array = Array(9) { IntArray(9) }
        for (row in 0..8) {
            for (col in 0..8) {
                val num = map[row]!![col]?.text.toString().toIntOrNull()
                if (num == null) {
                    array[row][col] = 0
                } else {
                    array[row][col] = num
                }
            }
        }
        return array
    }

    private fun setSudoku(arr: Array<IntArray>) {

        for (row in 0..8) {
            for (col in 0..8) {
                if (arr[row][col] == 0) {
                    map[row]!![col]?.setText("")
                } else {
                    map[row]!![col]?.setText(arr[row][col].toString())
                }
            }
        }
    }

}


