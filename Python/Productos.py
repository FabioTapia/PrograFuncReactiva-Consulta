class Producto:
    def __init__(self, nombre, categoria, precio, stock):
        self.nombre = nombre
        self.categoria = categoria
        self.precio = precio
        self.stock = stock

    def __str__(self):
        return f"Producto({self.nombre}, {self.categoria}, {self.precio}, {self.stock})"


productos = [
    Producto("Laptop", "Electrónica", 1200.0, 15),
    Producto("Mouse", "Electrónica", 25.0, 100),
    Producto("Teclado", "Electrónica", 75.0, 50),
    Producto("Silla", "Muebles", 150.0, 30),
    Producto("Escritorio", "Muebles", 300.0, 20),
    Producto("Monitor", "Electrónica", 400.0, 25),
    Producto("Lámpara", "Muebles", 45.0, 60),
    Producto("Audífonos", "Electrónica", 80.0, 75),
    Producto("Cuaderno", "Oficina", 5.0, 200),
    Producto("Bolígrafo", "Oficina", 2.0, 500)
]


# --- TIPO 1: Selección (filter) ---
print("--- TIPO 1: Selección (filter) ---")
productos_con_stock_alto = list(filter(lambda p: p.stock > 50, productos))
for p in productos_con_stock_alto:
    print(f"{p.nombre} - Stock: {p.stock}")


# --- TIPO 2: Transformación (map) ---
print("\n--- TIPO 2: Transformación (map) ---")
precios_con_iva = list(map(lambda p: (p.nombre, p.precio * 1.16), productos))
for nombre, precio in precios_con_iva:
    print(f"{nombre}: ${precio:.2f}")


# --- TIPO 3: Validación (any / all) ---
print("\n--- TIPO 3: Validación (any / all) ---")
hay_agotados = any(p.stock == 0 for p in productos)
print(f"¿Hay productos agotados? {hay_agotados}")

todos_con_precio_positivo = all(p.precio > 0 for p in productos)
print(f"¿Todos tienen precio positivo? {todos_con_precio_positivo}")


# --- TIPO 4: Extremos (max / min con key) ---
print("\n--- TIPO 4: Extremos (max / min) ---")
producto_mas_caro = max(productos, key=lambda p: p.precio)
print(f"Producto más caro: {producto_mas_caro.nombre} (${producto_mas_caro.precio})")

producto_menos_stock = min(productos, key=lambda p: p.stock)
print(f"Producto con menos stock: {producto_menos_stock.nombre} ({producto_menos_stock.stock} unidades)")
