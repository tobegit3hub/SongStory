$(document).ready(function(){

    $("#header_icon").click(function(){
	
	$("#header_link").toggle("slow");
    });

    $("#header_title").click(function(){

	$("#header_link").toggle("slow");
    });

    
    $("#footer_text_highlight").click(function(){

	alert("tobe tobeg3oogle@gmail.com\nkiic  282383419@qq.com\nbintou 40272902\
9@qq.com\neasyhard easyhard@qq.com");

    });


    var imgWrapper = $('.screenshots > img');
    // only show the first image, hide the rest
    imgWrapper.hide().filter(':first').show();
    $('ul.choose_list li a').click(function () {
	// check if this item doesn't have class "current"
	// if it has class "current" it must not execute the script again
	if (this.className.indexOf('current') == -1){
	    imgWrapper.hide();
	    imgWrapper.filter(this.hash).fadeIn(500);
	    $('ul.choose_list li a').removeClass('current');
	    $(this).addClass('current');
	}
	return false;
    });


});
