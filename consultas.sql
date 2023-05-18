/* Todos los productos del rubro "librería", creados hoy */
SELECT producto.* FROM producto JOIN rubro ON rubro.id_rubro=producto.id_rubro
WHERE rubro.rubro="libreria" AND producto.fecha_creacion=current_date;

/* Monto total vendido por cliente (mostrar nombre del cliente y monto) */
SELECT cliente.nombre, sum(cantidad*precio_unitario) FROM venta JOIN cliente ON cliente.id_cliente = venta.id_cliente GROUP BY cliente.nombre;

/* Cantidad de ventas por producto */
SELECT nombre, sum(cantidad) FROM venta JOIN producto ON producto.codigo = venta.codigo_producto GROUP BY nombre;


/* Cantidad de productos diferentes comprados por cliente en el mes actual */
SELECT nombre, count(distinct codigo_producto) as 'productos diferentes' FROM cliente
JOIN venta ON venta.id_cliente=cliente.id_cliente WHERE MONTH(fecha)=MONTH(current_date) GROUP BY cliente.id_cliente;


/* Ventas que tienen al menos un producto del rubro "bazar" */
SELECT venta.* FROM venta
JOIN producto ON producto.codigo = venta.codigo_producto
JOIN rubro ON producto.id_rubro = rubro.id_rubro WHERE rubro.rubro="bazar";

/* Rubros que no tienen ventas en los últimos 2 meses */
SELECT rubro.rubro, max(fecha) as 'ultima venta' FROM rubro
JOIN producto ON producto.id_rubro=rubro.id_rubro
JOIN venta ON venta.codigo_producto = producto.codigo GROUP BY rubro.rubro
HAVING max(fecha)<date_add(CURRENT_DATE, interval -2 month);
