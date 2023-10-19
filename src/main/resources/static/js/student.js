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


});