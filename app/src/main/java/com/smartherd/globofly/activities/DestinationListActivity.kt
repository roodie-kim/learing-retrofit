package com.smartherd.globofly.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.smartherd.globofly.R
import com.smartherd.globofly.helpers.DestinationAdapter
import com.smartherd.globofly.helpers.SampleData
import com.smartherd.globofly.models.Destination
import com.smartherd.globofly.services.DestinationService
import com.smartherd.globofly.services.ServiceBuilder
import kotlinx.android.synthetic.main.activity_destiny_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DestinationListActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_destiny_list)

		setSupportActionBar(toolbar)
		toolbar.title = title

		fab.setOnClickListener {
			val intent = Intent(this@DestinationListActivity, DestinationCreateActivity::class.java)
			startActivity(intent)
		}
	}

	override fun onResume() {
		super.onResume()

		loadDestinations()
	}

	private fun loadDestinations() {
        // To be replaced by retrofit code
		val destinationService: DestinationService = ServiceBuilder.buildService(DestinationService::class.java)

        val filter = HashMap<String, String>()
//        filter["country"] = "India"
//        filter["count"] = "1"

		val requestCall: Call<List<Destination>> = destinationService.getDestinationList(filter)

		requestCall.enqueue(object: Callback<List<Destination>> {

			override fun onResponse(call: Call<List<Destination>>, response: Response<List<Destination>>) {
				if (response.isSuccessful) {
					val destinationList: List<Destination> = response.body()!!
					destiny_recycler_view.adapter = DestinationAdapter(destinationList)
				} else if (response.code() == 401) {
                    Toast.makeText(this@DestinationListActivity,
                        "Your session has expored. Please login again", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@DestinationListActivity, "Failed to retrieve items", Toast.LENGTH_SHORT).show()
                }
			}

			override fun onFailure(call: Call<List<Destination>>, t: Throwable) {
                Toast.makeText(this@DestinationListActivity, "Error Ocurred ${t.toString()}", Toast.LENGTH_SHORT).show()
			}
		})
    }
}
