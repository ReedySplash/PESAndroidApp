package edu.pes.laresistencia.activity


interface IActivityView {

    fun showActivity(activity: ActivityRequest)
    fun showServerError()
    fun reloadRecyclerView(listComments: List<CommentsRequest>)
    fun resetCommentEditText()
    fun showPostCommentError()
    fun showGetCommentsError()

}