function submitSearchForm() {
    // ????????????????????? :)
    document.getElementById('#search-form').submit();
}

function addIndexToTable() {
    var table = document.getElementsByClassName("numbered-table");
    var rows = table[0].getElementsByTagName("tr");

    for (var i = 0; i < rows.length; i++) {
        var cell = rows[i].insertCell(0);
        if (i > 0) {
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
                    window.location.href = "list" + "?success";
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

$("body").on("click", ".delete-lecturer", function () {
    var topicId = $(this).attr("id");
    handleDeletion("/librarian/lectures/delete/" + topicId, 'Lecturer is deleted');
});


$(document).on("click", ".remove-lecturer", function(e) {
    e.preventDefault();  // Prevent the default link behavior
    var courseId = $(this).data("course-id");
    var url = '/librarian/courses/' + courseId + '/remove-lecture';

    Swal.fire({
        title: 'Are you sure?',
        text: "Do you want to remove this lecturer?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, remove it!'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                type: "GET",
                url: url,
                // Add CSRF token if needed
                success: function() {
                    Swal.fire(
                        'Removed!',
                        'The lecturer has been removed.',
                        'success'
                    ).then(() => {
                        window.location.reload(); // Reload the page or redirect
                    });
                },
                error: function() {
                    Swal.fire('Error', 'Failed to remove the lecturer.', 'error');
                }
            });
        }
    });
});
function confirmAddLecturer() {
    // Get the selected lecturer's email from the dropdown
    const lecturerEmail = document.getElementById('lecturerEmail').value;

    // Get the course name from the hidden input field
    const courseName = document.getElementById('courseName').value;

    // Display confirmation dialog using SweetAlert
    Swal.fire({
        title: 'Are you sure?',
        html: `Do you want to assign <b>${lecturerEmail}</b> to the course <b>${courseName}</b>?<br><br>This action will send an email to the lecturer.`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, assign it!'
    }).then((result) => {
        if (result.isConfirmed) {
            // Submit the form if the user confirms
            document.getElementById('addLecturerForm').submit();
        }
    });
}


function resizeTextarea(id) {
    var element = document.getElementById(id);
    element.style.height = 'auto';
    element.style.height = (element.scrollHeight) + 'px';
}

