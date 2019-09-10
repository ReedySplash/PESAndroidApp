package edu.pes.laresistencia.listactivities



interface IListActivitiesView {

    fun showServerError()
    fun initActivities(listactivities: List<ActivityData>?)

}