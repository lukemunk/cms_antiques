function replaceTags(html){
	$('#tags').replaceWith($(html));
}

$('button[name="addTag"]').click(function(event){
	var data = $('form').serialize();
	$.post('/add_tag', data, replaceTags);
	return false;
});


function clicked(index){
	var data = $('form').serialize();
	data += '&removeTag=' + index;
	
	$.post('/remove_tag', data, replaceTags);
	
	return false;
}