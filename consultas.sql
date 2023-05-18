''' Todos los productos del rubro "librería", creados hoy '''
select producto.* from producto join rubro on rubro.id_rubro=producto.id_rubro
where rubro.rubro="libreria" and producto.fecha_creacion=current_date;

''' Monto total vendido por cliente (mostrar nombre del cliente y monto) '''
select cliente.nombre, sum(cantidad*precio_unitario) from venta join cliente on cliente.id_cliente = venta.id_cliente group by cliente.nombre;

''' Cantidad de ventas por producto '''
select nombre, sum(cantidad) from venta join producto on producto.codigo = venta.codigo_producto group by nombre;


''' Cantidad de productos diferentes comprados por cliente en el mes actual '''
select nombre, count(distinct codigo_producto)as 'productos diferentes' from cliente
join venta on venta.id_cliente=cliente.id_cliente where MONTH(fecha)=MONTH(current_date) group by cliente.id_cliente;


''' Ventas que tienen al menos un producto del rubro "bazar" '''
select venta.* from venta
join producto on producto.codigo = venta.codigo_producto
join rubro on producto.id_rubro = rubro.id_rubro where rubro.rubro="bazar";

''' Rubros que no tienen ventas en los últimos 2 meses '''
select rubro.rubro, max(fecha) as 'ultima venta' from rubro
join producto on producto.id_rubro=rubro.id_rubro
join venta on venta.codigo_producto = producto.codigo GROUP BY rubro.rubro
HAVING max(fecha)<date_add(CURRENT_DATE, interval -2 month);
