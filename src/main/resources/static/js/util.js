/**
 * 
 */
inventario = document.querySelector('#inventario');
/**Convertir las cadenas en mayúsculas */
$(document).ready(function() {
	$("input").on("keypress", function() {
		$input = $(this);
		setTimeout(function() {
			$input.val($input.val().toUpperCase());
		}, 50);
	});
	$("textarea").on("keypress", function() {
		$textarea = $(this);
		setTimeout(function() {
			$textarea.val($textarea.val().toUpperCase());
		}, 50);
	});

})

/**Permite sólo números */
inventario.precioCompra.addEventListener('keypress', function(e) {
	if (!soloNumeros(event)) {
		e.preventDefault();
	}
});
inventario.precioVenta.addEventListener('keypress', function(e) {
	if (!soloNumeros(event)) {
		e.preventDefault();
	}
});
//Solo permite introducir numeros.
function soloNumeros(e) {
	var key = e.charCode;
	//console.log(key);
	return (key >= 48 && key <= 57) || key == 46;
}