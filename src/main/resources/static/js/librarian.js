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
// Hàm xử lý xóa chung
    function handleDeletion(url, successMessage) {
        Swal.fire({
            title: 'Do you want to delete this item?',
            showCancelButton: true,
            showDenyButton: true,
            confirmButtonText: 'Delete',
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    type: "GET",
                    url: url,
                    success: function () {
                        // Xử lý thành công
                        window.location.reload(); // Hoặc chuyển hướng đến trang khác
                        Swal.fire('Deleted!', successMessage, 'success');
                    },
                    error: function () {
                        // Xử lý lỗi
                        Swal.fire('Error', 'Failed to delete the item.', 'error');
                    }
                });
            } else if (result.isDenied) {
                Swal.fire('Cancelled', 'Deletion was canceled.', 'info');
            }
        });
    }

// Xóa tài khoản khi nhấn vào một phần tử có class "delete-account"
    $("body").on("click", ".delete-account", function () {
        var accountId = $(this).attr("id");
        handleDeletion(`/librarian/accounts/${accountId}/delete`, 'Account is deleted');
    });

// Xóa khóa học khi nhấn vào một phần tử có class "delete-course"
    $("body").on("click", ".delete-course", function () {
        var courseId = $(this).attr("id");
        handleDeletion(`/librarian/courses/delete/${courseId}`, 'Course is deleted');
    });

// Xóa chủ đề khi nhấn vào một phần tử có class "delete-topic"
    $("body").on("click", ".delete-topic", function () {
        var topicId = $(this).attr("id");
        handleDeletion(`/librarian/courses/deleteTopic/${topicId}`, 'Topic is deleted');
    });

});