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