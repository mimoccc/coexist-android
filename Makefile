

debug:
	@ant debug

release:
	@ant release

clean:
	@ant clean
	@git checkout master -f
