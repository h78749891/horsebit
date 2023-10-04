    package com.a406.horsebit

    import android.os.Bundle
    import android.text.Editable
    import android.text.TextWatcher
    import android.util.Log
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.ArrayAdapter
    import android.widget.Spinner
    import androidx.preference.PreferenceManager
    import com.a406.horsebit.databinding.FragmentOrderBuyTabBinding
    import com.a406.horsebit.databinding.FragmentStockOrderBinding
    import retrofit2.Call
    import retrofit2.Callback
    import retrofit2.Response
    import java.util.Locale

    class OrderBuyTabFragment : Fragment() {

        private lateinit var binding : FragmentOrderBuyTabBinding
        private lateinit var orderNum: Spinner
        val api = APIS.create()

        var tokenNo: Long = 0L
        var code: String = ""

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_order_buy_tab, container, false)

            binding = FragmentOrderBuyTabBinding.bind(view)

            tokenNo = arguments?.getLong("tokenNo") ?: 0
            code = arguments?.getString("code") ?: ""

            binding.tvOrderBuyNumType.text = code
            binding.tvOrderCanBuyPrice.text = "0 KRW"

            val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())  // import androidx.preference.PreferenceManager 인지 확인
            val token: String = pref.getString("SERVER_ACCESS_TOKEN", "1") ?: "1"

            api.MyTotalAsset(authorization = "Bearer ${token}").enqueue(object: Callback<MyTotalAssetResponseBodyModel> {
                override fun onResponse(call: Call<MyTotalAssetResponseBodyModel>, response: Response<MyTotalAssetResponseBodyModel>) {
                    if(response.code() == 200) {    // 200 Success
                        Log.d("로그", "매수 주문 요청: 200 Success")

                        val responseBody = response.body()

                        if(responseBody != null) {
                            binding.tvOrderCanBuyPrice.text = "${responseBody.cashBalance} KRW"
                        }
                    }
                    else if(response.code() == 201) {   // 201 Created
                        Log.d("로그", "매수 주문 요청: 400 Bad Request")
                    }
                    else if(response.code() == 202) {   // 202 Accepted - 요청은 정상이나 아직 처리 중
                        Log.d("로그", "매수 주문 요청: 400 Bad Request")
                    }
                    else if(response.code() == 400) {   // 400 Bad Request - Message에 누락 필드명 기입
                        Log.d("로그", "매수 주문 요청: 400 Bad Request")
                    }
                    else if(response.code() == 401) {   // 401 Unauthorized - 인증 토큰값 무효
                        Log.d("로그", "매수 주문 요청: 401 Unauthorized")
                    }
                    else if(response.code() == 403) {   // 403 Forbidden - 권한 없음 (둘러보기 회원)
                        Log.d("로그", "매수 주문 요청: 400 Bad Request")
                    }
                    else if(response.code() == 404) {   // 404 Not Found
                        Log.d("로그", "매수 주문 요청: 404 Not Found")
                    }
                }
                override fun onFailure(call: Call<MyTotalAssetResponseBodyModel>, t: Throwable) {
                    Log.d("로그", "매수 주문 요청: onFailure")
                }
            })

            binding.etOrderBuyNum.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    calculateTotalPrice()
                }
            })

            binding.etOrderBuyPrice.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    calculateTotalPrice()
                }
            })

            binding.tvOrderBuyCommit.setOnClickListener {

                val requestData = OrderRequestRequestBodyModel(
                    tokenNo,
                    binding.etOrderBuyNum.text.toString().toDouble(),
                    binding.etOrderBuyPrice.text.toString().toLong()
                )

                api.orderRequest(authorization = "Bearer ${token}" , requestData).enqueue(object: Callback<OrderRequestResponseBodyModel> {
                    override fun onResponse(call: Call<OrderRequestResponseBodyModel>, response: Response<OrderRequestResponseBodyModel>) {
                        if(response.code() == 200) {    // 200 Success
                            Log.d("로그", "매수 주문 요청: 200 Success")

                            val responseBody = response.body()

                        }
                        else if(response.code() == 201) {   // 201 Created
                            Log.d("로그", "매수 주문 요청: 201 Created")
                        }
                        else if(response.code() == 202) {   // 202 Accepted - 요청은 정상이나 아직 처리 중
                            Log.d("로그", "매수 주문 요청: 202 Accepted")
                        }
                        else if(response.code() == 400) {   // 400 Bad Request - Message에 누락 필드명 기입
                            Log.d("로그", "매수 주문 요청: 400 Bad Request")
                        }
                        else if(response.code() == 401) {   // 401 Unauthorized - 인증 토큰값 무효
                            Log.d("로그", "매수 주문 요청: 401 Unauthorized")
                        }
                        else if(response.code() == 403) {   // 403 Forbidden - 권한 없음 (둘러보기 회원)
                            Log.d("로그", "매수 주문 요청: 400 Bad Request")
                        }
                        else if(response.code() == 404) {   // 404 Not Found
                            Log.d("로그", "매수 주문 요청: 404 Not Found")
                        }
                    }
                    override fun onFailure(call: Call<OrderRequestResponseBodyModel>, t: Throwable) {
                        Log.d("로그", "매수 주문 요청: onFailure")
                    }
                })
            }

            return view
        }

        private fun calculateTotalPrice() {
            val orderNumText = binding.etOrderBuyNum.text.toString()
            val orderPriceText = binding.etOrderBuyPrice.text.toString()

            val orderNum = orderNumText.toDoubleOrNull() ?: 0.0
            val orderPrice = orderPriceText.toDoubleOrNull() ?: 0.0

            val totalPrice = orderNum * orderPrice

            val formattedTotalPrice = String.format(Locale.US, "%.0f", totalPrice)

            binding.tvOrderBuyTotalPrice.text = formattedTotalPrice
        }
    }