
SOURCES= \
	kitapyakala/Yakala.scala \
	kitapyakala/pipelines/DummyBookDB.scala \
	kitapyakala/pipelines/GoogleAppEngineBookDB.scala \
	kitapyakala/spiders/PandoraSpider.scala \
	kitapyakala/spiders/IdefixSpider.scala \
	kitapyakala/spiders/KitapyurduSpider.scala \
	kitapyakala/spiders/ImgeSpider.scala

CP=-cp .:yakala.jar:jsoup-1.6.1.jar
FLAGS=-deprecation -unchecked

all: clean build run

clean:
	-find . -name "*.class" -exec rm {} \;

build:
	fsc $(FLAGS) $(CP) $(SOURCES)

run:
	time scala -cp ./yakala.jar:./jsoup-1.6.1.jar:. kitapyakala.Yakala idefix.com imge.com.tr
