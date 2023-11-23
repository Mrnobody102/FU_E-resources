/*
       PAGINATION
    */
// Account pagination
$(".page-account-number").click(function () {

    const search = $("#search-text").val();
    let pageIndex = $(this).html();

    window.location = "/admin/accounts/list/" + pageIndex + "?search=" + search;
});

$(".previous-page-account").click(function () {
    const search = $("#search-text").val();
    const pageIndex = $(".active .page-account-number").text();
    const currentPage = parseInt(pageIndex);
    if (currentPage > 1) {
        window.location.href = "/admin/accounts/list/" + (currentPage - 1) + "?search=" + search;
    }
});

$(".next-page-account").click(function () {
    const search = $("#search-text").val();
    const pageIndex = $(".active .page-account-number").text();
    const currentPage = parseInt(pageIndex);
    window.location.href = "/admin/accounts/list/" + (currentPage + 1) + "?search=" + search;
});

// DELETE
// Function to handle deletion with Swal alert
function handleDeletion(url, successRedirectUrl, successMessage) {
    Swal.fire({
        title: 'Do you want to delete this item?',
        showCancelButton: true,
        confirmButtonText: 'Delete',
        icon: 'warning',
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                type: "POST", // or "DELETE"
                url: url,
                // include CSRF token if necessary
                success: function () {
                    Swal.fire('Deleted!', successMessage, 'success').then(() => {
                        window.location.href = successRedirectUrl;
                    });
                },
                error: function () {
                    Swal.fire('Error', 'Failed to delete the item.', 'error');
                }
            });
        } else if (result.dismiss === Swal.DismissReason.cancel) {
            Swal.fire('Cancelled', 'Deletion was canceled.', 'info');
        }
    });
}

// Usage example
$("body").on("click", ".delete-account", function () {
    var accountId = $(this).attr("id");
    handleDeletion("/admin/accounts/" + accountId + "/delete", '/admin/accounts/list', 'Account is deleted');
});

$("body").on("click", ".delete-training-type", function () {
    var tid = $(this).attr("id");
    handleDeletion("/admin/trainingtypes/delete/" + tid, '/admin/trainingtypes/list', 'Training type is deleted');
});

