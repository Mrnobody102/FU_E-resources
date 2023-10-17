// Document scope
$(document).ready(function() {

    $(".page-course-number").click(function () {

        var search = $("#search-text").val();
        var major = $("#major").val();
        var pageIndex = $(this).html();

        window.location = "/lecturer/courses/list/" + pageIndex + "?search=" + search +"&major=" +major;

    });

    // Delete course
    $("body").on("click", ".delete-course", function() {
        var courseId = $(this).attr("id");
        var result = confirm("Do you want delete this courses?" + courseId);

        if(result){
            window.location = "/librarian/courses/delete/"+courseId;
        }
    });

    // Delete topic
    $("body").on("click", ".delete-topic", function() {
        var courseTopic = $(this).attr("id");
        var result = confirm("Do you want delete this topic?"+ courseTopic);
        if(result){
            window.location = "/librarian/courses/deleteTopic"+courseTopic;
        }
    });
});