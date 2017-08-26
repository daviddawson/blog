
all: docker

docker: clean build
	docker build -t back-end .

build:
	./gradlew bootRepackage

clean:
	./gradlew clean

release: clean build
	docker build . -t mytown-backend
	docker tag mytown-backend simplicityitself-muon-image.jfrog.io/mytown-backend:${BUILD_NUMBER}
	docker push simplicityitself-muon-image.jfrog.io/mytown-backend:${BUILD_NUMBER}
