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

        window.location = "/librarian/courses/list/" + pageIndex + "?search=" + search;

    });

    $(".page-account-number").click(function () {

        var search = $("#search-text").val();
        var pageIndex = $(this).html();

        window.location = "/librarian/accounts/list/" + pageIndex + "?search=" + search;

    });
// Function to handle deletion with Swal alert
    function handleDeletion(url, successMessage) {
        var currentPage = window.location.href;
        var currentPageWithoutSuccess = currentPage.replace(/[?&]success(=[^&]*)?(&|$)/, '');

        Swal.fire({
            title: 'Do you want to delete this item?',
            showCancelButton: true,
            showDenyButton: true,
            confirmButtonText: 'Delete',
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    type: "GET",
                    url: url + "?currentPage=" + encodeURIComponent(currentPageWithoutSuccess),
                    success: function () {
                        window.location.href = currentPageWithoutSuccess + "?success";
                        Swal.fire('Deleted!', successMessage, 'success');
                    },
                    error: function () {
                        Swal.fire('Error', 'Failed to delete the item.', 'error');
                    }
                });
            } else if (result.isDenied) {
                Swal.fire('Cancelled', 'Deletion was canceled.', 'info');
            }
        });
    }

// Handle delete account click
    $("body").on("click", ".delete-account", function () {
        var accountId = $(this).attr("id");
        handleDeletion("/librarian/accounts/" + accountId + "/delete", 'Account is deleted');
    });

// Handle delete course click
    $("body").on("click", ".delete-course", function () {
        var courseId = $(this).attr("id");
        handleDeletion("/librarian/courses/delete/" + courseId, 'Course is deleted');
    });

// Handle delete topic click
    $("body").on("click", ".delete-topic", function () {
        var topicId = $(this).attr("id");
        handleDeletion("/librarian/courses/deleteTopic/" + topicId, 'Topic is deleted');
    });

});