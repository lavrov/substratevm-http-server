default:
	docker build -t substratevm-server:0.0.1 .

run:
	docker run -it --rm -p 8080:8080 substratevm-server:0.0.1