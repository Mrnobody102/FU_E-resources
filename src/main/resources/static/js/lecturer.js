// Document scope
$(document).ready(function() {
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