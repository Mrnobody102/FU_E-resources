$(document).ready(function () {

    // Navbar when choose
    let path = window.location.pathname;
    $('.header__navbar-list > li > a[href="' + path + '"]').removeClass('header__navbar-item-main-link');
    $('.header__navbar-list > li > a[href="' + path + '"]').addClass('header__navbar-item-main-link_active');
    $('.header__navbar-list > li > a[href="' + path + '"]').parent().addClass('header__navbar-main-item_active');

    /*=============== SHOW SIDEBAR ===============*/
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

    /*=============== LINK ACTIVE ===============*/
    const sidebarLink = document.querySelectorAll('.sidebar__link')

    function linkColor() {
        sidebarLink.forEach(l => l.classList.remove('active-link'))
        this.classList.add('active-link')
    }

    sidebarLink.forEach(l => l.addEventListener('click', linkColor))
});