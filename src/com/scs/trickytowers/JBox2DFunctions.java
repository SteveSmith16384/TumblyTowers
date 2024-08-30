package com.scs.trickytowers;


import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;


public class JBox2DFunctions {

	public static Body AddRectangle(BodyUserData bud, World world, float centre_x, float centre_y, float width, float height,
			BodyType bodytype, float restitution, float friction, float weight_kgm2) {
		PolygonShape ps = new PolygonShape();
		ps.setAsBox(width / 2, height / 2);

		FixtureDef fd = new FixtureDef();
		fd.shape = ps;

		// Melhorando os cálculos dos parâmetros
		fd.restitution = adjustRestitutionForRectangles(restitution);
		fd.friction = adjustFrictionForRectangles(friction);
		fd.density = adjustDensityForRectangles(weight_kgm2);

		BodyDef bd = new BodyDef();
		bd.type = bodytype;
		bd.position = new Vec2(centre_x, centre_y);

		Body b = world.createBody(bd);
		Fixture f = b.createFixture(fd);

		f.setUserData(bud);
		b.setUserData(bud);

		return b;
	}

	public static Body CreateComplexShape(BodyUserData bud, World world, Vec2[] vertices,
										  BodyType bodytype, float restitution, float friction, float weight_kgm2) {
		PolygonShape ps = new PolygonShape();
		ps.set(vertices, vertices.length);

		FixtureDef fd = new FixtureDef();
		fd.shape = ps;

		// Melhorando os cálculos dos parâmetros
		fd.restitution = adjustRestitutionForComplexShapes(restitution);
		fd.friction = adjustFrictionForComplexShapes(friction);
		fd.density = adjustDensityForComplexShapes(weight_kgm2);

		BodyDef bd = new BodyDef();
		bd.type = bodytype;

		Body b = world.createBody(bd);
		b.createFixture(fd);
		b.setUserData(bud);

		return b;
	}

	public static Body AddEdgeShapeByTL(World world, BodyUserData bud, float x1, float y1, float x2, float y2,
										BodyType bodyType, float restitution, float friction, float weight_kgm2) {
		EdgeShape es = new EdgeShape();
		es.set(new Vec2(0, 0), new Vec2(x2 - x1, y2 - y1));

		FixtureDef fixtureDef = new FixtureDef();

		// Melhorando os cálculos dos parâmetros
		fixtureDef.density = adjustDensityForEdges(weight_kgm2);
		fixtureDef.restitution = adjustRestitutionForEdges(restitution);
		fixtureDef.friction = adjustFrictionForEdges(friction);
		fixtureDef.shape = es;

		BodyDef bd = new BodyDef();
		bd.type = bodyType;
		bd.position = new Vec2(x1, y1);

		Body b = world.createBody(bd);
		b.createFixture(fixtureDef);
		b.setUserData(bud);
		return b;
	}

	private static float adjustDensityForEdges(float density) {
		// Ajuste para bordas: baixa densidade para simular limites estáticos ou rígidos
		return Math.max(0.1f, Math.min(density, 0.5f)); // Valores entre 0.1 e 0.5 para bordas
	}

	private static float adjustRestitutionForEdges(float restitution) {
		// Ajuste de restituição: valores baixos para simular bordas que não rebatem
		return Math.max(0.0f, Math.min(restitution, 0.3f)); // Valores entre 0.0 e 0.3
	}

	private static float adjustFrictionForEdges(float friction) {
		// Ajuste de fricção: fricção média-alta para bordas
		return Math.max(0.4f, Math.min(friction, 0.8f)); // Valores entre 0.4 e 0.8
	}

	private static float adjustDensityForRectangles(float density) {
		// Ajuste para retângulos: densidade média para representar blocos sólidos ou móveis
		return Math.max(0.5f, Math.min(density, 2.0f)); // Valores entre 0.5 e 2.0
	}

	private static float adjustRestitutionForRectangles(float restitution) {
		// Ajuste de restituição: valores médios-baixos para simular objetos que não quicam muito
		return Math.max(0.1f, Math.min(restitution, 0.4f)); // Valores entre 0.1 e 0.4
	}

	private static float adjustFrictionForRectangles(float friction) {
		// Ajuste de fricção: fricção média para garantir resistência ao deslizamento
		return Math.max(0.3f, Math.min(friction, 0.7f)); // Valores entre 0.3 e 0.7
	}

	private static float adjustDensityForComplexShapes(float density) {
		// Ajuste para formas complexas: densidade média a alta para manter estabilidade
		return Math.max(0.8f, Math.min(density, 2.5f)); // Valores entre 0.8 e 2.5
	}

	private static float adjustRestitutionForComplexShapes(float restitution) {
		// Ajuste de restituição: valores médios para permitir algum "quique", dependendo da forma
		return Math.max(0.1f, Math.min(restitution, 0.6f)); // Valores entre 0.1 e 0.6
	}

	private static float adjustFrictionForComplexShapes(float friction) {
		// Ajuste de fricção: fricção variável para diferentes tipos de superfícies complexas
		return Math.max(0.3f, Math.min(friction, 0.7f)); // Valores entre 0.3 e 0.7
	}
}
