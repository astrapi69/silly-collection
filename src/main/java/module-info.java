module silly.collection.main {
	requires jobj.core.main;
	requires org.apache.commons.collections4;
	requires comparator.extensions.main;
	requires silly.bean.main;
	exports io.github.astrapi69.collection;
	exports io.github.astrapi69.collection.array;
	exports io.github.astrapi69.collection.enumeration;
	exports io.github.astrapi69.collection.iterator;
	exports io.github.astrapi69.collection.list;
	exports io.github.astrapi69.collection.map;
	exports io.github.astrapi69.collection.modification;
	exports io.github.astrapi69.collection.properties;
	exports io.github.astrapi69.collection.set;
}