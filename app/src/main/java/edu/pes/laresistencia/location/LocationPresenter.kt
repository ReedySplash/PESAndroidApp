package edu.pes.laresistencia.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.view.View
import com.google.android.gms.maps.model.Marker
import edu.pes.laresistencia.injection.modules.AppModule
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.service.LocationService
import edu.pes.laresistencia.injection.component.DaggerLocationInjector
import edu.pes.laresistencia.network.LocationAPI
import javax.inject.Inject


enum class LocationType {
    food, lodging, clothes, hygiene, medicine
}

class LocationPresenter(val context: Context, val view: ILocationView) {

    @Inject
    lateinit var locationAPI: LocationAPI

    private val locationService: LocationService
    private lateinit var response: List<Location>
    private var visibility: Boolean
    private var descriptionMap: HashMap<String, String>

    init {
        val injector = DaggerLocationInjector
                .builder()
                .appModule(AppModule(context))
                .networkModule(NetworkModule)
                .build()
        injector.inject(this)
        locationService = LocationService(locationAPI)
        descriptionMap = HashMap()
        visibility = true
    }



    private fun handleResponse(response: List<Location>, foodOption: Boolean = true, lodgingOption: Boolean = true,
                               clothesOption: Boolean = true, hygieneOption: Boolean = true, medicineOption: Boolean = true) {

        for (point in response) {
            when (point.kind) {
                LocationType.food.toString() -> {
                    if (foodOption)
                        view.showFoodLocationPoint(point.name, point.latitude, point.longitude, point.address)
                }
                LocationType.lodging.toString() -> {
                    if (lodgingOption)
                        view.showLodgingLocationPoint(point.name, point.latitude, point.longitude, point.address)
                }
                LocationType.clothes.toString() -> {
                    if (clothesOption)
                        view.showClothesLocationPoint(point.name, point.latitude, point.longitude, point.address)
                }
                LocationType.hygiene.toString() -> {
                    if (hygieneOption)
                        view.showHygieneLocationPoint(point.name, point.latitude, point.longitude, point.address)
                }
                LocationType.medicine.toString() -> {
                    if (medicineOption)
                        view.showMedicineLocationPoint(point.name, point.latitude, point.longitude, point.address)
                }
            }
            descriptionMap[point.name] = point.description
        }
        view.centerCamera()
    }

    fun preparePoints() {
        val allPoints: List<Location>? = locationService.getAllPoints()
        if (allPoints != null) {
            response = allPoints
            handleResponse(response)
        }
        else view.showErrorToast()
    }

    fun filterButtonPressed() {
        view.showLocationOptionsView()
    }

    fun optionsSelected(foodOption: Boolean, lodgingOption: Boolean, clothesOption: Boolean, hygieneOption: Boolean, medicineOption: Boolean) {
        view.resetLocationPoints()
        handleResponse(response, foodOption, lodgingOption, clothesOption, hygieneOption, medicineOption)
        view.updateCheckedOptions(foodOption, lodgingOption, clothesOption, hygieneOption, medicineOption)
    }

    fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            view.makeRequest()
        }
    }

    fun handleMarkerClicked(marker: Marker?) {
        if (marker != null) {
            var description = ""
            if (descriptionMap[marker.title] != null)
                description = descriptionMap[marker.title]!!
            view.manageDescriptionViewNames(marker.title, description)
            view.manageVisibility(View.VISIBLE)
        }
    }

    fun onMapClicked() {
        view.manageVisibility(View.INVISIBLE)
    }

}