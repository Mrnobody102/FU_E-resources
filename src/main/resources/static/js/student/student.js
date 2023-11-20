function unsavedCourse(param){
    var courseId = $(this).attr("courseId");
    var loading = "<p><a th:attr=\"courseId=${courseId}\">\n" +
        "                                        <i class=\"fas fa-spinner fa-spin\"></i>\n" +
        "                                        UnBookmarking</a></p>"
    $(".stu_save-course-link").html(loading);
    $.get({
        url: '/api/student/courses/'+ courseId + '/unsaved_course',
        success: function(responseData) {
            console.log(responseData);
            var saved = "<p><a class=\"unsaved-course\" courseId=" + courseId + ">" +
                "                                        <i class=\"fa-solid fa-bookmark\"></i>\n" +
                "                                        Unbookmark</a>\n" +
                "                                    </p>";
            var unsaved = "<p><a class=\"save-course\" courseId=" + courseId + ">" +
                "                                        <i class=\"fa-regular fa-bookmark\"></i>\n" +
                "                                        Bookmark</a></p>";


            if('saved'== responseData){
                $(".stu_save-course-link").html(saved);
            }else if(unsaved){
                $(".stu_save-course-link").html(unsaved);
            }
        },
        error: function(errorData) {
        }
    })
}

function saveCourse(param){
    var courseId = $(this).attr("courseId");
    var loading = "<p><a th:attr=\"courseId=${courseId}\">\n" +
        "                                        <i class=\"fas fa-spinner fa-spin\"></i>\n" +
        "                                        Bookmarking</a></p>"
    $(".stu_save-course-link").html(loading);
    $.get({
        url: '/api/student/courses/'+ courseId + '/save_course',
        success: function(responseData) {
            console.log(responseData);
            var saved = "<p><a class=\"unsaved-course\" courseId=" + courseId + ">" +
                "                                        <i class=\"fa-solid fa-bookmark\"></i>\n" +
                "                                        Unbookmark</a>\n" +
                "                                    </p>";
            var unsaved = "<p><a class=\"save-course\" courseId=" + courseId + ">" +
                "                                        <i class=\"fa-regular fa-bookmark\"></i>\n" +
                "                                        Bookmark</a></p>";
            if('saved'== responseData){
                $(".stu_save-course-link").html(saved);
            }else if(unsaved){
                $(".stu_save-course-link").html(unsaved);
            }
        },
        error: function(errorData) {
        }
    })
}

$(document).ready(function () {

    /*
        NAVBAR
     */

    // Navbar when choose
    let path = window.location.pathname;
    let $targetElements = $('.header__navbar-list > li > a[href="' + path + '"]');

    $targetElements.removeClass('header__navbar-item-main-link');
    $targetElements.addClass('header__navbar-item-main-link_active');
    $targetElements.parent().addClass('header__navbar-main-item_active');

    /*
        SIDEBAR
     */

    // Show sidebar
    const showSidebar = (toggleId, sidebarId, mainId) => {
        const toggle = document.getElementById(toggleId),
            sidebar = document.getElementById(sidebarId),
            main = document.getElementById(mainId)

        if (toggle && sidebar && main) {
            toggle.addEventListener('click', () => {
                /* Show sidebar */
                sidebar.classList.toggle('show-sidebar')
                /* Add padding main */
                main.classList.toggle('main-pd')

            })
        }
    }
    showSidebar('toggle', 'sidebar', 'main')

    // Link active
    const sidebarLink = document.querySelectorAll('.sidebar__link')

    function linkColor() {
        sidebarLink.forEach(l => l.classList.remove('active-link'))
        this.classList.add('active-link')
    }

    sidebarLink.forEach(l => l.addEventListener('click', linkColor))


    /*
        PAGINATION
     */

    // Account pagination
    $(".page-stu-course-number").click(function () {

        const search = $("#search-text").val();
        const filter = $("#filter").val();
        let pageIndex = $(this).html();

        window.location = "/student/search_course/" + pageIndex + "?search=" + search + "&filter=" +filter;
    });

    $(".previous-page-stu-course-number").click(function () {
        const search = $("#search-text").val();
        const filter = $("#filter").val();
        const pageIndex = $(".pagination-item--active .page-stu-course-number").text();
        const currentPage = parseInt(pageIndex);
        if (currentPage > 1) {
            window.location.href = "/student/search_course/" + (currentPage - 1) + "?search=" + search + "&filter=" + filter;
        }
    });

    $(".next-page-stu-course-number").click(function () {
        const search = $("#search-text").val();
        const filter = $("#filter").val();
        const pageIndex = $(".pagination-item--active .page-stu-course-number").text();
        const currentPage = parseInt(pageIndex);
        window.location.href = "/student/search_course/" + (currentPage + 1) + "?search=" + search+ "&filter=" + filter;
    });


    // Account pagination
    $(".page-stu-note-number").click(function () {

        const search = $("#search-text").val();
        const filter = $("#filter").val();
        let pageIndex = $(this).html();

        window.location = "/student/my_library/my_notes/" + pageIndex + "?search=" + search + "&filter=" +filter;
    });

    $(".previous-page-stu-note-number").click(function () {
        const search = $("#search-text").val();
        const filter = $("#filter").val();
        const pageIndex = $(".pagination-item--active .page-stu-note-number").text();
        const currentPage = parseInt(pageIndex);
        if (currentPage > 1) {
            window.location.href = "/student/my_library/my_notes/" + (currentPage - 1) + "?search=" + search + "&filter=" + filter;
        }
    });

    $(".next-page-stu-note-number").click(function () {
        const search = $("#search-text").val();
        const filter = $("#filter").val();
        const pageIndex = $(".pagination-item--active .page-stu-note-number").text();
        const currentPage = parseInt(pageIndex);
        window.location.href = "/student/my_library/my_notes/" + (currentPage + 1) + "?search=" + search+ "&filter=" + filter;
    });


    $("body").on("click", ".save-doc", function() {
        var docId = $(this).attr("docId");
        var loading = "<p><a th:attr=\"docId=${docId}\">\n" +
            "                                        <i class=\"fas fa-spinner fa-spin\"></i>\n" +
            "                                        Bookmarking</a></p>"
        $(".stu_save-doc-link").html(loading);
        $.get({
            url: '/api/student/documents/'+ docId + '/save_document',
            success: function(responseData) {
                console.log(responseData);
                var saved = "<p><a class=\"unsaved-doc\" docId=" + docId + ">" +
                    "                                        <i class=\"fa-solid fa-bookmark\"></i>\n" +
                    "                                        Unbookmark</a>\n" +
                    "                                    </p>";
                var unsaved = "<p><a class=\"save-doc\" docId=" + docId + ">" +
                    "                                        <i class=\"fa-regular fa-bookmark\"></i>\n" +
                    "                                        Bookmark</a></p>";
                if('saved'== responseData){
                    $(".stu_save-doc-link").html(saved);
                }else if(unsaved){
                    $(".stu_save-doc-link").html(unsaved);
                }
            },
            error: function(errorData) {
            }
        })
    });
    $("body").on("click", ".unsaved-doc", function() {
        var docId = $(this).attr("docId");
        var loading = "<p><a th:attr=\"docId=${docId}\">\n" +
            "                                        <i class=\"fas fa-spinner fa-spin\"></i>\n" +
            "                                        UnBookmarking</a></p>"
        $(".stu_save-doc-link").html(loading);
        $.get({
            url: '/api/student/documents/'+ docId + '/unsaved_document',
            success: function(responseData) {
                console.log(responseData);
                var saved = "<p><a class=\"unsaved-doc\" docId=" + docId + ">" +
                    "                                        <i class=\"fa-solid fa-bookmark\"></i>\n" +
                    "                                        Unbookmark</a>\n" +
                    "                                    </p>";
                var unsaved = "<p><a class=\"save-doc\" docId=" + docId + ">" +
                    "                                        <i class=\"fa-regular fa-bookmark\"></i>\n" +
                    "                                        Bookmark</a></p>";


                if('saved'== responseData){
                    $(".stu_save-doc-link").html(saved);
                }else if(unsaved){
                    $(".stu_save-doc-link").html(unsaved);
                }
            },
            error: function(errorData) {
            }
        })
    });
    $("body").on("click", ".save-course", function() {
        var courseId = $(this).attr("courseId");
        var loading = "<p><a th:attr=\"courseId=${courseId}\">\n" +
            "                                        <i class=\"fas fa-spinner fa-spin\"></i>\n" +
            "                                        Bookmarking</a></p>"
        $(".stu_save-course-link").html(loading);
        $.get({
            url: '/api/student/courses/'+ courseId + '/save_course',
            success: function(responseData) {
                console.log(responseData);
                var saved = "<p><a class=\"unsaved-course\" courseId=" + courseId + ">" +
                    "                                        <i class=\"fa-solid fa-bookmark\"></i>\n" +
                    "                                        Unbookmark</a>\n" +
                    "                                    </p>";
                var unsaved = "<p><a class=\"save-course\" courseId=" + courseId + ">" +
                    "                                        <i class=\"fa-regular fa-bookmark\"></i>\n" +
                    "                                        Bookmark</a></p>";
                if('saved'== responseData){
                    $(".stu_save-course-link").html(saved);
                }else if(unsaved){
                    $(".stu_save-course-link").html(unsaved);
                }
            },
            error: function(errorData) {
            }
        })
    })

    $("body").on("click", ".unsaved-course", function() {
        var courseId = $(this).attr("courseId");
        var loading = "<p><a th:attr=\"courseId=${courseId}\">\n" +
            "                                        <i class=\"fas fa-spinner fa-spin\"></i>\n" +
            "                                        UnBookmarking</a></p>"
        $(".stu_save-course-link").html(loading);
        $.get({
            url: '/api/student/courses/'+ courseId + '/unsaved_course',
            success: function(responseData) {
                console.log(responseData);
                var saved = "<p><a class=\"unsaved-course\" courseId=" + courseId + ">" +
                    "                                        <i class=\"fa-solid fa-bookmark\"></i>\n" +
                    "                                        Unbookmark</a>\n" +
                    "                                    </p>";
                var unsaved = "<p><a class=\"save-course\" courseId=" + courseId + ">" +
                    "                                        <i class=\"fa-regular fa-bookmark\"></i>\n" +
                    "                                        Bookmark</a></p>";


                if('saved'== responseData){
                    $(".stu_save-course-link").html(saved);
                }else if(unsaved){
                    $(".stu_save-course-link").html(unsaved);
                }
            },
            error: function(errorData) {
            }
        })
    })

});