/*
       PAGINATION
    */
// Account pagination
$(".page-account-number").click(function () {

    const search = $("#search-text").val();
    let pageIndex = $(this).html();
    const role = $('#role').val();
    window.location = "/admin/accounts/list/" + pageIndex + "?search=" + search + "&role=" + role;
});

$(".previous-page-account").click(function () {
    const search = $("#search-text").val();
    const pageIndex = $(".active .page-account-number").text();
    const currentPage = parseInt(pageIndex);
    if (currentPage > 1) {
        const role = $('#role').val();
        window.location.href = "/admin/accounts/list/" + (currentPage - 1) + "?search=" + search + "&role=" + role;
    }
});

$(".next-page-account").click(function () {
    const search = $("#search-text").val();
    const pageIndex = $(".active .page-account-number").text();
    const currentPage = parseInt(pageIndex);
    const role = $('#role').val();
    window.location.href = "/admin/accounts/list/" + (currentPage + 1) + "?search=" + search + "&role=" + role;
});

// DELETE
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
$("body").on("click", ".delete-account", function () {
    var accountId = $(this).attr("id");
    handleDeletion("/admin/accounts/" + accountId + "/delete", 'Account is deleted');
});