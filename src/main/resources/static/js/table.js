function initializeDataTable(selector, customOptions) {
    var defaultOptions = {
        "searching": true,
        "ordering": false,
        "pagingType": "simple_numbers",
        "pageLength": 10,
        "language": {
            "paginate": {
                "first": "<<",
                "last": ">>",
                "next": ">",
                "previous": "<"
            }
        }
    };

    var options = $.extend(true, defaultOptions, customOptions);

    var dataTable = $(selector).DataTable(options);

    // Thêm bất kỳ cài đặt hoặc hàm bổ sung nào
    // Ví dụ: debounce function, event listeners, etc.

    return dataTable;
}
