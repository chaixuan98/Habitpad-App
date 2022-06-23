package com.example.habitpadapplication.utils


import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.habitpadapplication.SessionManager
import com.example.habitpadapplication.listener.StepListener
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class StepDetector {

    private val ACCEL_RING_SIZE = 50
    private val VEL_RING_SIZE = 10

    // change this threshold according to your sensitivity preferences
    private val STEP_THRESHOLD = 50f

    private val STEP_DELAY_NS = 250000000

    private var accelRingCounter = 0
    private val accelRingX = FloatArray(ACCEL_RING_SIZE)
    private val accelRingY = FloatArray(ACCEL_RING_SIZE)
    private val accelRingZ = FloatArray(ACCEL_RING_SIZE)
    private var velRingCounter = 0
    private val velRing = FloatArray(VEL_RING_SIZE)
    private var lastStepTimeNs: Long = 0
    private var oldVelocityEstimate = 0f
    val STEP2METERS = 0.715f
    private var todayStepCount: Long = 0
    private var listener: StepListener? = null
    var dateFormat = SimpleDateFormat("yyyy-MM-dd")
    private var context : Context
    private var requestQueue: RequestQueue? = null
    var userID : String = ""

    var sessionManager: SessionManager? = null
    val MyPREFERENCES = "MyPrefs"
    var sharedPreferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    val KEY_USERID = "userID"




    constructor(context : Context){
       this.context =  context;


        sessionManager = SessionManager(context)
        sessionManager!!.checkLogin()
        val usersDetails: HashMap<String, String> = sessionManager!!.getUsersDetailFromSession()

        userID = usersDetails[SessionManager.KEY_USERID].toString()
    }

    fun registerListener(listener: StepListener) {
        this.listener = listener
    }



    fun updateAccelerometer(timeNs: Long, x: Float, y: Float, z: Float) {

        val currentAccel = FloatArray(3)
        currentAccel[0] = x
        currentAccel[1] = y
        currentAccel[2] = z

        // First onStepAndDistance is to update our guess of where the global z vector is.
        accelRingCounter++
        accelRingX[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[0]
        accelRingY[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[1]
        accelRingZ[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[2]

        val worldZ = FloatArray(3)
        worldZ[0] = SensorFilter().sum(accelRingX) / Math.min(accelRingCounter, ACCEL_RING_SIZE)
        worldZ[1] = SensorFilter().sum(accelRingY) / Math.min(accelRingCounter, ACCEL_RING_SIZE)
        worldZ[2] = SensorFilter().sum(accelRingZ) / Math.min(accelRingCounter, ACCEL_RING_SIZE)

        val normalization_factor = SensorFilter().norm(worldZ)

        worldZ[0] = worldZ[0] / normalization_factor
        worldZ[1] = worldZ[1] / normalization_factor
        worldZ[2] = worldZ[2] / normalization_factor

        val currentZ = SensorFilter().dot(worldZ, currentAccel) - normalization_factor
        velRingCounter++
        velRing[velRingCounter % VEL_RING_SIZE] = currentZ

        val velocityEstimate = SensorFilter().sum(velRing)
        //Log.e("Cnesor_event", "changeed  here =" + timeNs)


        if (velocityEstimate > STEP_THRESHOLD && oldVelocityEstimate <= STEP_THRESHOLD
                && timeNs - lastStepTimeNs > STEP_DELAY_NS) {



            var todayDateString = dateFormat.format(Date(System.currentTimeMillis()))

            ReadStepToServer(userID,todayDateString);


            val distance = (todayStepCount * STEP2METERS)/1000 //Distance in meter

            val cal = todayStepCount * 0.04


            WriteStepToServer(userID,todayStepCount, distance, cal, todayDateString)

            listener!!.onStepAndDistance(todayStepCount, distance)


            lastStepTimeNs = timeNs
        }

        oldVelocityEstimate = velocityEstimate


    }

    fun ReadStepToServer(userID: String, date: String) {
        var volleyRequestQueue: RequestQueue? = null
        val serverAPIURL: String = "http://192.168.0.133/android/get_user_today_step.php"
        volleyRequestQueue = Volley.newRequestQueue(context)

        val parameters: MutableMap<String, String> = HashMap()
        // Add your parameters in HashMap
        parameters.put("userID", userID.toString());
        parameters.put("stepDate",date);

        val strReq: StringRequest = object : StringRequest(Method.POST,serverAPIURL,
                Response.Listener { response ->
                    Log.e(TAG, "response: " + response)

                    // Handle Server response here
                    try {
                        val responseObj = JSONObject(response)

                        val success = responseObj.getString("success")
                        val jsonArray: JSONArray = responseObj.getJSONArray("userStep")

                        if (success == "1") {
                            for (i in 0 until jsonArray.length()) {
                                var jsonInner: JSONObject = jsonArray.getJSONObject(i)
                                todayStepCount = jsonInner.getLong("totalStep")

                                todayStepCount = todayStepCount + 1L
                            }
                        }
                        else{
                            todayStepCount = 1L
                        }
                    } catch (e: Exception) { // caught while parsing the response
                        Log.e(TAG, "problem occurred")
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { volleyError -> // error occurred
                    Log.e(TAG, "problem occurred, volley error: " + volleyError.message)
                }) {

            override fun getParams(): MutableMap<String, String> {
                return parameters;
            }

        }
        // Adding request to request queue
        volleyRequestQueue?.add(strReq)
    }


    fun WriteStepToServer(userID:String, step: Long, distance: Float, cal: Double, date: String) {
        var volleyRequestQueue: RequestQueue? = null
        val serverAPIURL: String = "http://192.168.0.133/android/edit_user_step.php"
        volleyRequestQueue = Volley.newRequestQueue(context)

        val parameters: MutableMap<String, String> = HashMap()
        // Add your parameters in HashMap
        parameters.put("userID", userID.toString());
        parameters.put("totalStep", step.toString());
        parameters.put("stepDistance", distance.toString());
        parameters.put("stepCal", cal.toString());
        parameters.put("stepDate",date);

        val strReq: StringRequest = object : StringRequest(Method.POST,serverAPIURL,
                Response.Listener { response ->
                    Log.e(TAG, "response: " + response)

                    // Handle Server response here
                    try {

                    } catch (e: Exception) { // caught while parsing the response
                        Log.e(TAG, "problem occurred")
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { volleyError -> // error occurred
                    Log.e(TAG, "problem occurred, volley error: " + volleyError.message)
                }) {

            override fun getParams(): MutableMap<String, String> {
                return parameters;
            }

        }
        // Adding request to request queue
        volleyRequestQueue?.add(strReq)
    }

    fun logoutUserFromStepSession() {
        val prefs = getDefaultSharedPreferences(context)
        prefs.edit().clear().commit()

        sessionManager!!.logoutUserFromSession();

    }

}