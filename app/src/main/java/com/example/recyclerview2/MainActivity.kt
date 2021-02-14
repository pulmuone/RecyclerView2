package com.example.recyclerview2

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.SparseBooleanArray
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview2.databinding.ActivityMainBinding
import com.example.recyclerview2.databinding.Row2Binding


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
/*
    var imgRes = intArrayOf(R.drawable.imgflag1,R.drawable.imgflag2,R.drawable.imgflag3,R.drawable.imgflag4,R.drawable.imgflag5,R.drawable.imgflag6,R.drawable.imgflag7,R.drawable.imgflag8,R.drawable.imgflag1,R.drawable.imgflag2,R.drawable.imgflag3,R.drawable.imgflag4,R.drawable.imgflag5,R.drawable.imgflag6,R.drawable.imgflag7,R.drawable.imgflag8)
    var data1 = arrayOf("토고", "프랑스", "스위스", "스페인", "일본", "독일", "브라질", "대한민국","토고", "프랑스", "스위스", "스페인", "일본", "독일", "브라질", "대한민국")
    var qty1 = arrayOf("1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1")
*/
    var productLst =  mutableListOf<ProductModel>()

    private val adapter = RecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        for(i in 0..30) {
            productLst.add(ProductModel(i.toLong(), "88091234$i", "상품명 상품명 상품명 $i", i))
        }

        //val adapter1 = RecyclerAdapter()
        binding.recycler1.adapter = adapter

        productLst.add(ProductModel(999, "111111", "콜라 180ml", 56))
        //adapter.notifyDataSetChanged()
        binding.recycler1.layoutManager = LinearLayoutManager(this)

    }


    inner class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolderClass>() {
        var mSelectedItems = SparseBooleanArray(0)
        var row_index = -1

        inner class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            /*
            val rowImageView = itemView.findViewById<ImageView>(R.id.rowImageView)
            val rowTextView =  itemView.findViewById<TextView>(R.id.rowTextView)
            val rowButton =  itemView.findViewById<TextView>(R.id.rowButton)
            val rowEditText =  itemView.findViewById<TextView>(R.id.rowEditText)
            */
            var rowBarcode = itemView.findViewById<TextView>(R.id.rowBarcode)
            var rowBarcodeName = itemView.findViewById<TextView>(R.id.rowBarcodeName)
            var rowButtonMinus = itemView.findViewById<Button>(R.id.rowButtonMinus)
            var rowEditQty = itemView.findViewById<EditText>(R.id.rowEditQty)
            var rowButtonPlus = itemView.findViewById<Button>(R.id.rowButtonPlus)

            override fun onClick(v: View?) {
                binding.textView.text = productLst[adapterPosition].name
                //val intent = Intent(applicationContext, DetailActivity::class.java)
                //startActivity(intent)

                mSelectedItems.put(row_index, false)
                notifyItemChanged(row_index)

                toggleItemSelected(adapterPosition)
                row_index = adapterPosition
            }
        }

        //항목 구성을 위해 사용할  ViewHolder 객체가 필요할 때 호출되는 메소드
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
            //val itemView = layoutInflater.inflate(R.layout.row, null)
            val itemView = Row2Binding.inflate(layoutInflater)

            val view = itemView.root
            val holder = ViewHolderClass(view)

            holder.rowButtonMinus.setOnClickListener {
                //binding.textView.text = "버튼 클릭 ${data1[holder.adapterPosition]}"
                holder.rowEditQty.setText((Integer.parseInt(holder.rowEditQty.text.toString()) - 1).toString())

                mSelectedItems.put(row_index, false)
                notifyItemChanged(row_index)
                toggleItemSelected(holder.adapterPosition)
                row_index = holder.adapterPosition
            }

            holder.rowEditQty.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    if (p0 != null && p0.toString() != "") {
                        //holder.adapterPosition
                        productLst[holder.adapterPosition].Qty = Integer.parseInt(p0.toString())
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })

            holder.rowEditQty.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //Clear focus here from edittext
                    holder.rowEditQty.clearFocus()
                    //holder.rowEditQty.setFocusableInTouchMode(false);
                    //holder.rowEditQty.setFocusable(false);
                }
                false
            })

            holder.rowEditQty.setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus) {
                    mSelectedItems.put(row_index, false)
                    notifyItemChanged(row_index)

                    toggleItemSelected(holder.adapterPosition)
                    row_index = holder.adapterPosition
                    Log.d("test Focus", holder.adapterPosition.toString())
                    //v.isSelected = true
                } else {
                    //v.isSelected = false

                }
            }

            holder.rowButtonPlus.setOnClickListener {
                //binding.textView.text = "버튼 클릭 ${data1[holder.adapterPosition]}"
                holder.rowEditQty.setText((Integer.parseInt(holder.rowEditQty.text.toString()) + 1).toString())

                mSelectedItems.put(row_index, false)
                notifyItemChanged(row_index)
                toggleItemSelected(holder.adapterPosition)
                row_index = holder.adapterPosition
            }

            view.setOnClickListener(holder)

            return holder
        }

        //ViewHolder를 통해 항목을 구성할 때 항목 내의 View 객체에 데이터를 셋팅한다.
        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
            //holder.rowImageView.setImageResource(imgRes[position])
            holder.rowBarcode.text = productLst[position].barcode.toString()
            holder.rowBarcodeName.text = productLst[position].name.toString()
            holder.rowEditQty.setText(productLst[position].Qty.toString())

            holder.itemView.isSelected = isItemSelected(position)
        }

        override fun getItemCount(): Int {
            return productLst.size
        }

        fun toggleItemSelected(position: Int) {
            if (mSelectedItems.get(position, false) == true) {
                mSelectedItems.delete(position)
                notifyItemChanged(position)
            } else {
                mSelectedItems.put(position, true)
                notifyItemChanged(position)
            }
        }

        fun isItemSelected(position: Int) : Boolean {
            return mSelectedItems.get(position, false)
        }


        fun clearSelectedItem() {
            var position : Int

            for (i in 0 .. mSelectedItems.size()) {
                position = mSelectedItems.keyAt(i)
                mSelectedItems.put(position, false)
                notifyItemChanged(position)
            }
            mSelectedItems.clear()
        }

    }
}
