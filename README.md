# Patio de Comidas

Sistema de escritorio en Java para gestionar un patio de comidas: registro de
restaurantes y sus platos, toma de pedidos por cliente y consulta de totales.

Proyecto bimestral de **Programación Orientada a Objetos** — Universidad Técnica
Particular de Loja (UTPL), 2026.

## Tecnologías

- **Java** — lenguaje base
- **Swing** — interfaz gráfica de escritorio
- **Serialización de Java** (`ObjectOutputStream`) — persistencia en `patio.dat`
- **Apache Ant** — compilación y empaquetado
- **NetBeans** — entorno de desarrollo

## Arquitectura

El proyecto sigue una separación en tres capas:

```
Solucion_Codigo/src/
├── modelo/       # entidades del dominio
│   ├── PatioComidas.java   # agregado raíz: contiene restaurantes y clientes
│   ├── Restaurante.java
│   ├── Plato.java
│   ├── Ingrediente.java
│   ├── Cliente.java
│   ├── Pedido.java
│   └── DetallePedido.java
├── vista/        # interfaz de usuario
│   ├── VentanaPrincipal.java
│   ├── Main.java
│   └── PruebaModelo.java
└── controlador/  # lógica de aplicación y persistencia
    ├── Gestor.java        # orquesta las operaciones sobre el modelo
    └── Repositorio.java   # guarda y carga el estado (patrón DAO)
```

`Repositorio` aísla la persistencia del resto del programa: el modelo no sabe
que se está serializando a un archivo, así que cambiar el almacenamiento no
obliga a tocar las entidades.

## Cómo ejecutarlo

Requisitos: JDK 8 o superior y Apache Ant.

```bash
cd Solucion_Codigo
ant run      # compilar y ejecutar
ant jar      # generar el .jar
```

También se puede abrir la carpeta `Solucion_Codigo` como proyecto en NetBeans y
ejecutar con F6.

## Modelado

El diagrama de clases está en `Modelado UML/DiagramaClases.png` (fuente `.dia`).

## Autores

Proyecto desarrollado en pareja por [@pabloarmijos11](https://github.com/pabloarmijos11)
y [@21andree](https://github.com/21andree).
