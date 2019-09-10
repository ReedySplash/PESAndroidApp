package edu.pes.laresistencia.location


interface ILocationView {

    fun showFoodLocationPoint(name: String, latitude:Double, longitude: Double, address: String)
    fun showLodgingLocationPoint(name: String, latitude:Double, longitude: Double, address: String)
    fun showClothesLocationPoint(name: String, latitude:Double, longitude: Double, address: String)
    fun showHygieneLocationPoint(name: String, latitude:Double, longitude: Double, address: String)
    fun showMedicineLocationPoint(name: String, latitude:Double, longitude: Double, address: String)
    fun centerCamera()
    fun showLocationOptionsView()
    fun resetLocationPoints()
    fun updateCheckedOptions(foodOption: Boolean, lodgingOption: Boolean, clothesOption: Boolean, hygieneOption: Boolean, medicineOption: Boolean)
    fun showErrorToast()
    fun makeRequest()
    fun manageVisibility(visibility: Int)
    fun manageDescriptionViewNames(title: String, description: String)

}