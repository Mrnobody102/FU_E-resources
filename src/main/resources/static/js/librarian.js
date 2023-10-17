function submitSearchForm (){
   document.getElementById('#search-form').submit();
}

$(document).ready(function () {

    // show is Admin
    $('#role').change(function() {
        if ($(this).val() === 'LIBRARIAN') {
            $('#isAdminCheckbox').show();
        } else {
            $('#isAdminCheckbox').hide();
        }
    });

    // Pagination

    $(".page-course-number").click(function () {

        var search = $("#search-text").val();
        var pageIndex = $(this).html();
        var major = $("#major").val();

        window.location = "/librarian/courses/list/" + pageIndex + "?search=" + search +"&major=" +major;

    });

    $(".page-account-number").click(function () {

        var search = $("#search-text").val();
        var pageIndex = $(this).html();

        window.location = "/librarian/accounts/list/" + pageIndex + "?search=" + search;

    });

    $("body").on("click", ".delete-course", function () {
        var courseId = $(this).attr("id");
        var result = confirm("Do you want delete this courses?" + courseId);

        if (result) {
            window.location = "/librarian/courses/delete/" + courseId;
        }
    });

    $("body").on("click", ".delete-topic", function () {
        var courseTopic = $(this).attr("id");
        var result = confirm("Do you want delete this topic?" + courseTopic);
        if (result) {
            window.location = "/librarian/courses/deleteTopic" + courseTopic;
        }
    });
});