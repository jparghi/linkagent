.PHONY: sanity run stop clean

sanity:
	./sanity.sh

run:
	mvn spring-boot:run

stop:
	@PID=$$(lsof -ti :8080); \
	if [ -n "$$PID" ]; then \
		echo "Stopping app on port 8080 (PID $$PID)..."; \
		kill -9 $$PID; \
	else \
		echo "No app running on port 8080."; \
	fi

clean:
	mvn clean install
