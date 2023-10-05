/**
 * 
 */
$(document).ready(function () {
    $(".home-course__list").slick({
        slidesToShow: 5,
        slidesToScroll: 2,
        arrows: true,
        prevArrow: "<button type='button' class='slick-prev pull-left'><i class='fa fa-angle-left' aria-hidden='true'></i></button>",
        nextArrow: "<button type='button' class='slick-next pull-right'><i class='fa fa-angle-right' aria-hidden='true'></i></button>",
        infinite: false
    });
});