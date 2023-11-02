function submitSearchForm() {
    // ????????????????????? :)
    document.getElementById('#search-form').submit();
}
function addIndexToTable(){
    var table = document.getElementsByClassName("numbered-table");
    var rows = table[0].getElementsByTagName("tr");

    for (var i = 0; i < rows.length; i++) {
        var cell = rows[i].insertCell(0);
        if( i>0 ){
            cell.textContent = i;
        }

    }
}

    /*
        PAGINATION
     */

    // Course pagination
    $(".page-course-number").click(function () {
        const search = $("#search-text").val();
        const pageIndex = $(this).html();

        window.location = "/librarian/courses/list/" + pageIndex + "?search=" + search;

    });

    $(".previous-page-course").click(function () {
        const search = $("#search-text").val();
        const pageIndex = $(".active .page-course-number").text();
        const currentPage = parseInt(pageIndex);
        if (currentPage > 1) {
            window.location.href = "/librarian/courses/list/" + (currentPage - 1) + "?search=" + search;
        }
    });

    $(".next-page-course").click(function () {
        const search = $("#search-text").val();
        const pageIndex = $(".active .page-course-number").text();
        const currentPage = parseInt(pageIndex);
        window.location.href = "/librarian/courses/list/" + (currentPage + 1) + "?search=" + search;
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

    /*
        DELETE
     */

    // Handle delete course click
    $("body").on("click", ".delete-course", function () {
        var courseId = $(this).attr("id");
        handleDeletion("/librarian/courses/" + courseId + "/delete", 'Course is deleted');
    });

    // Handle delete topic click
    $("body").on("click", ".delete-topic", function () {
        var topicId = $(this).attr("id");
        handleDeletion("/librarian/courses/deleteTopic/" + topicId, 'Topic is deleted');
    });

// });