function viewQuestion(){
    $("#note").css("display", "none");
    $("#question").css("display", "block");
    $("#link-view-notes").removeClass("stu__navbar-active")
    $("#link-view-questions").addClass("stu__navbar-active");
}
function viewNote(){
    $("#note").css("display", "block");
    $("#question").css("display", "none");
    $("#link-view-questions").removeClass("stu__navbar-active")
    $("#link-view-notes").addClass("stu__navbar-active");
}
$(document).ready(

    $("body").on("click", ".add-note", function() {
        var contextPath = $(this).attr("contextPath");
        var id = $(this).attr("docId");
        alert(contextPath + "/student/note/add");

        $.get({
            dataType: 'JSON',
            url: contextPath + "/student/note/add/" + id,
            success: function(responseData) {
                /*$(".stu__note-content").html(responseData.content);*/
                alert(responseData.message);

            },
            error: function(errorData) {
                $(".card-body").html(errorData);
            }
        })
    })
);
