<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard - Gestión de Inventario</title>
    <meta charset="UTF-8">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: Arial, sans-serif; background: #f5f5f5; }
        .container { max-width: 1200px; margin: 0 auto; padding: 20px; }
        h1 { margin-bottom: 30px; color: #333; }
        .kpi-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; margin-bottom: 30px; }
        .kpi-card { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); transition: transform 0.2s; }
        .kpi-card:hover { transform: translateY(-5px); box-shadow: 0 4px 8px rgba(0,0,0,0.15); }
        .kpi-card h3 { color: #666; font-size: 14px; margin-bottom: 10px; text-transform: uppercase; }
        .kpi-card .value { font-size: 36px; font-weight: bold; color: #2196F3; }
        .kpi-card.warning .value { color: #ff9800; }
        .kpi-card.danger .value { color: #f44336; }
        .nav { background: #2196F3; padding: 15px 20px; margin-bottom: 20px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        .nav a { color: white; text-decoration: none; margin-right: 20px; font-weight: 500; padding: 8px 12px; border-radius: 4px; transition: background 0.3s; }
        .nav a:hover { background: rgba(255,255,255,0.2); }
        .nav a.active { background: rgba(255,255,255,0.3); }
        .loading { text-align: center; padding: 40px; color: #666; }
        .error { background: #ffebee; color: #c62828; padding: 15px; border-radius: 4px; margin-bottom: 20px; }
        .config { background: white; padding: 15px; border-radius: 8px; margin-bottom: 20px; }
        .config label { margin-right: 10px; font-weight: 500; }
        .config input { padding: 8px; border: 1px solid #ddd; border-radius: 4px; width: 80px; }
        .config button { padding: 8px 16px; background: #2196F3; color: white; border: none; border-radius: 4px; cursor: pointer; margin-left: 10px; }
        .config button:hover { background: #1976D2; }
    </style>
</head>
<body>
<div class="nav">
    <a href="dashboard.jsp" class="active">Dashboard</a>
    <a href="productos.jsp">Productos</a>
    <a href="categorias.jsp">Categorías</a>
    <a href="movimientos.jsp">Movimientos</a>
</div>

<div class="container">
    <h1>📊 Dashboard de Inventario</h1>

    <div class="config">
        <label for="umbral">Umbral de Stock Bajo:</label>
        <input type="number" id="umbral" value="10" min="1">
        <button onclick="cargarKPIs()">Actualizar</button>
    </div>

    <div id="errorContainer"></div>
    <div id="loadingContainer" class="loading">Cargando datos...</div>

    <div class="kpi-grid" id="kpiGrid" style="display:none;">
        <div class="kpi-card">
            <h3>📦 Total de Productos</h3>
            <div class="value" id="totalProductos">0</div>
        </div>
        <div class="kpi-card warning">
            <h3>⚠️ Stock Bajo</h3>
            <div class="value" id="stockBajo">0</div>
        </div>
        <div class="kpi-card danger">
            <h3>❌ Productos Inactivos</h3>
            <div class="value" id="inactivos">0</div>
        </div>
        <div class="kpi-card">
            <h3>🏷️ Total de Categorías</h3>
            <div class="value" id="totalCategorias">0</div>
        </div>
        <div class="kpi-card">
            <h3>📈 Movimientos (Semana)</h3>
            <div class="value" id="movimientosSemana">0</div>
        </div>
    </div>
</div>

<script>
    function getContextPath() {
        return '${pageContext.request.contextPath}';
    }

    function mostrarError(mensaje) {
        const errorDiv = document.getElementById('errorContainer');
        errorDiv.innerHTML = `<div class="error">❌ Error: ${mensaje}</div>`;
        document.getElementById('loadingContainer').style.display = 'none';
    }

    function cargarKPIs() {
        const umbral = document.getElementById('umbral').value;
        const errorDiv = document.getElementById('errorContainer');
        const loadingDiv = document.getElementById('loadingContainer');
        const kpiGrid = document.getElementById('kpiGrid');

        errorDiv.innerHTML = '';
        loadingDiv.style.display = 'block';
        kpiGrid.style.display = 'none';

        fetch(getContextPath() + '/api/dashboard/kpis?umbral=' + umbral)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error al cargar los KPIs: ' + response.status);
                }
                return response.json();
            })
            .then(data => {
                console.log('KPIs recibidos:', data);

                document.getElementById('totalProductos').textContent = data.totalProductos || 0;
                document.getElementById('stockBajo').textContent = data.productosStockBajo || 0;
                document.getElementById('inactivos').textContent = data.productosInactivos || 0;
                document.getElementById('totalCategorias').textContent = data.totalCategorias || 0;
                document.getElementById('movimientosSemana').textContent = data.movimientosSemana || 0;

                loadingDiv.style.display = 'none';
                kpiGrid.style.display = 'grid';
            })
            .catch(error => {
                console.error('Error:', error);
                mostrarError(error.message + '. Verifica que el servidor esté corriendo y la API funcione.');
            });
    }

    // Cargar KPIs al iniciar
    window.onload = function() {
        cargarKPIs();
    };
</script>
</body>
</html>