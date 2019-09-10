package edu.pes.laresistencia.location

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.PopupWindow
import android.widget.Toast
import com.example.api.laresistencia.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.location_options_layout.*
import kotlinx.android.synthetic.main.location_view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils


class LocationView : Fragment(), OnMapReadyCallback, ILocationView, GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var presenter: LocationPresenter
    private val zoom:Float
    private val centerPoint:LatLng
    private var isFoodChecked: Boolean
    private var isLodgingChecked: Boolean
    private var isClothesChecked: Boolean
    private var isHygieneChecked: Boolean
    private var isMedicineChecked: Boolean
    private val REQUEST_LOCATION_CODE:Int = 200
    private lateinit var activity: Activity


    init {
        isFoodChecked = true
        isLodgingChecked = true
        isClothesChecked = true
        isHygieneChecked = true
        isMedicineChecked = true
        centerPoint = LatLng(41.396411, 2.159811)
        zoom = 12.toFloat()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.location_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title = activity.getString(R.string.location_title)
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.map_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId)
        {
            R.id.action_filter -> {
                presenter.filterButtonPressed()
                return true
            }
            R.id.action_settings -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        presenter = LocationPresenter(activity, this)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapview) as SupportMapFragment
        mapFragment.getMapAsync(this)
        presenter.setupPermissions()
        location_description_view.visibility = View.INVISIBLE
    }



    override fun makeRequest() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_CODE)
    }

    private fun shouldSetLocationEnabled() {
        val permission = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_LOCATION_CODE -> {
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    shouldSetLocationEnabled()
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMarkerClickListener(this)
        map.setOnMapClickListener {
            presenter.onMapClicked()
        }
        shouldSetLocationEnabled()
        presenter.preparePoints()
    }

    override fun centerCamera() {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(centerPoint, zoom))
    }

    private fun showLocationPoint(name:String, latitude: Double, longitude: Double, address: String, icon: Int) {
        val point = LatLng(latitude, longitude)
        map.addMarker(MarkerOptions()
                .position(point)
                .title(name)
                .snippet(address)
                .icon(BitmapDescriptorFactory.fromResource(icon)))
    }

    override fun showFoodLocationPoint(name: String, latitude:Double, longitude: Double, address: String) {
        showLocationPoint(name, latitude, longitude, address, R.drawable.ic_food)
    }

    override fun showLodgingLocationPoint(name: String, latitude:Double, longitude: Double, address: String) {
        showLocationPoint(name, latitude, longitude, address, R.drawable.ic_lodging)
    }

    override fun showClothesLocationPoint(name: String, latitude:Double, longitude: Double, address: String) {
        showLocationPoint(name, latitude, longitude, address, R.drawable.ic_clothes)
    }

    override fun showHygieneLocationPoint(name: String, latitude:Double, longitude: Double, address: String) {
        showLocationPoint(name, latitude, longitude, address, R.drawable.ic_hygiene)
    }

    override fun showMedicineLocationPoint(name: String, latitude: Double, longitude: Double, address: String) {
        showLocationPoint(name, latitude, longitude, address, R.drawable.ic_medicine)
    }

    override fun showLocationOptionsView() {
        var dialog = Dialog(activity)
        dialog.setContentView(com.example.api.laresistencia.R.layout.location_options_layout)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.location_check_food.isChecked = isFoodChecked
        dialog.location_check_lodging.isChecked = isLodgingChecked
        dialog.location_check_clothes.isChecked = isClothesChecked
        dialog.location_check_hygiene.isChecked = isHygieneChecked
        dialog.location_check_medicine.isChecked = isMedicineChecked
        dialog.location_button_dialog.setOnClickListener {
            presenter.optionsSelected(
                    foodOption = dialog.location_check_food.isChecked,
                    lodgingOption = dialog.location_check_lodging.isChecked,
                    clothesOption = dialog.location_check_clothes.isChecked,
                    hygieneOption = dialog.location_check_hygiene.isChecked,
                    medicineOption = dialog.location_check_medicine.isChecked
            )
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun resetLocationPoints() {
        map.clear()
    }

    override fun updateCheckedOptions(foodOption: Boolean, lodgingOption: Boolean, clothesOption: Boolean, hygieneOption: Boolean, medicineOption: Boolean) {
        isFoodChecked = foodOption
        isLodgingChecked = lodgingOption
        isClothesChecked = clothesOption
        isHygieneChecked = hygieneOption
        isMedicineChecked = medicineOption
    }

    override fun showErrorToast() {
        Toast.makeText(activity, getString(R.string.location_error_occurred), Toast.LENGTH_SHORT).show()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = context as (Activity)
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        presenter.handleMarkerClicked(marker)
        return false
    }

    override fun manageVisibility(visibility: Int) {
        if (visibility == View.VISIBLE) {
            val bottomUp: Animation = AnimationUtils.loadAnimation(context, R.anim.bottom_up)
            location_description_view.startAnimation(bottomUp)
            location_description_view.visibility = View.VISIBLE
        }
        else if (location_description_view.visibility != View.INVISIBLE) {
            val bottomDown: Animation = AnimationUtils.loadAnimation(context, R.anim.bottom_down)
            location_description_view.startAnimation(bottomDown)
            location_description_view.visibility = View.INVISIBLE
        }
    }

    override fun manageDescriptionViewNames(title: String, description: String) {
        location_description_title.text = title
        location_description_text.text = description
    }

}