/**
 * 
 */
/**Convertir las cadenas en mayúsculas */
$(document).ready(function() {
	$("input").on("keypress", function() {
		$input = $(this);
		setTimeout(function() {
			$input.val($input.val().toUpperCase());
		}, 50);
	});
})
inventario = document.querySelector('#inventario');	    
/**Permite sólo números */
inventario.abono.addEventListener('keypress', function(e) {
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

function abonar(){
	let restante = parseFloat($("#restante").val());
	let abono = parseFloat($("#abono").val());
	
	if(restante === abono){
		alert("El abono es de $" + abono + "\nLa prenda queda liquidada");
	}else if(abono>restante){
		alert("El abono es mayor a lo que resta por pagar")
		return false;
	}else{
		alert("El abono es de $" + abono + "\nResta: $"+ (restante-abono));
	}
	
	inventario.submit();
}