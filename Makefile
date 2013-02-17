
SOURCES= \
	kitapyakala/Yakala.scala \
	kitapyakala/pipelines/DummyBookDB.scala \
	kitapyakala/pipelines/GoogleAppEngineBookDB.scala \
	kitapyakala/spiders/BaseBookSpider.scala \
	kitapyakala/spiders/PandoraSpider.scala \
	kitapyakala/spiders/IdefixSpider.scala \
	kitapyakala/spiders/KitapyurduSpider.scala \
	kitapyakala/spiders/IlknoktaSpider.scala \
	kitapyakala/spiders/ImgeSpider.scala

CP=-cp lib/*:.
FLAGS=-deprecation -unchecked

STORES= \
	pandora.com.tr \
	idefix.com \
	ilknokta.com \
	kitapyurdu.com \
	imge.com.tr 
#	netkitap.com \

all: clean build run

clean:
	-find . -name "*.class" -exec rm {} \;

build:
	fsc $(FLAGS) $(CP) $(SOURCES)

run:
	JAVA_OPTS="-Xms2g -Xmx3g -agentlib:tijmp" time scala $(CP) kitapyakala.Yakala $(STORES)
