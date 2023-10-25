// Document scope
$(document).ready(function() {

    $(".page-course-number").click(function () {

        var search = $("#search-text").val();
        var major = $("#major").val();
        var pageIndex = $(this).html();

        window.location = "/lecturer/courses/list/" + pageIndex + "?search=" + search +"&major=" +major;

    });

    // Delete course
    $("body").on("click", ".delete-course", function() {
        var courseId = $(this).attr("id");
        var result = confirm("Do you want delete this courses?" + courseId);

        if(result){
            window.location = "/lecturer/courses/delete/"+courseId;
        }
    });

    // Delete topic
    $("body").on("click", ".delete-topic", function() {
        var courseTopic = $(this).attr("id");
        var result = confirm("Do you want delete this topic?"+ courseTopic);
        if(result){
            window.location = "/lecturer/courses/deleteTopic"+courseTopic;
        }
    });
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
        showSidebar('toggleIdLecturer', 'sidebar-lecturer', 'main-lecturer')

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


});