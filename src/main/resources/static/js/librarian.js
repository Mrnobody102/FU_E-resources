function submitSearchForm() {
    // ????????????????????? :)
    document.getElementById('#search-form').submit();
}

$(document).ready(function () {

    // SHOW is Admin
    $('#role').change(function () {
        if ($(this).val() === 'LIBRARIAN') {
            $('#isAdminCheckbox').show();
        } else {
            $('#isAdminCheckbox').hide();
        }
    });


    /*
        PAGINATION
     */
    // Account pagination
    $(".page-account-number").click(function () {

        const search = $("#search-text").val();
        let pageIndex = $(this).html();

        window.location = "/librarian/accounts/list/" + pageIndex + "?search=" + search;
    });

    $(".previous-page-account").click(function () {
        const search = $("#search-text").val();
        const pageIndex = $(".active .page-account-number").text();
        const currentPage = parseInt(pageIndex);
        if (currentPage > 1) {
            window.location.href = "/librarian/accounts/list/" + (currentPage - 1) + "?search=" + search;
        }
    });

    $(".next-page-account").click(function () {
        const search = $("#search-text").val();
        const pageIndex = $(".active .page-account-number").text();
        const currentPage = parseInt(pageIndex);
        window.location.href = "/librarian/accounts/list/" + (currentPage + 1) + "?search=" + search;
    });

    // Course pagination
    $(".page-course-number").click(function () {
        const search = $("#search-text").val();
        const pageIndex = $(this).html();
        const major = $("#major").val();

        window.location = "/librarian/courses/list/" + pageIndex + "?search=" + search + "&major=" + major;

    });

    $(".previous-page-course").click(function () {
        const search = $("#search-text").val();
        const major = $("#major").val();
        const pageIndex = $(".active .page-course-number").text();
        const currentPage = parseInt(pageIndex);
        if (currentPage > 1) {
            window.location.href = "/librarian/courses/list/" + (currentPage - 1) + "?search=" + search + "&major=" + major;
        }
    });

    $(".next-page-course").click(function () {
        const search = $("#search-text").val();
        const major = $("#major").val();
        const pageIndex = $(".active .page-course-number").text();
        const currentPage = parseInt(pageIndex);
        window.location.href = "/librarian/courses/list/" + (currentPage + 1) + "?search=" + search + "&major=" + major;
    });


    /*
        DELETE
     */

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