/*
 * ClassFile.cpp
 *
 *  Created on: Jul 29, 2010
 *      Author: joe
 */

#include "ClassFile.h"

using namespace std;

ClassFile::ClassFile(ZipFile file) {

	zfilePtr = file.getData();
	readMagic();
	readVersion();
	readConstants();
	readAccessFlags();
	readInterfaces();
	readFields();
	readMethods();
	readAttributes();
}

/**
 * Read and verify class file magic
 */
void ClassFile::readMagic() {
	magic = *(uint32_t *)zfilePtr;
	if(magic != JAVA_MAGIC) {
		fprintf(stderr, "bad magic: %x", magic);
		throw(-1);
	}
	// advance file pointer
	zfilePtr += 4;
}

/*
 * Read and store class file version
 */
void ClassFile::readVersion() {
	minor_version = *(uint16_t *)zfilePtr;
	major_version = *(uint16_t *)(zfilePtr+2);
	printf("class version: %d:%d", major_version, minor_version);
	zfilePtr += 4;
}

/*
 * Read in the constant pool
 */
void ClassFile::readConstants() {
	constant_pool_count = *(uint16_t *)zfilePtr;
	zfilePtr += 2;
	constantPool.add(this);

}

void ClassFile::readAccessFlags() {
	access_flags = *(uint16_t *)zfilePtr;
	zfilePtr += 2;
}

void ClassFile::readClassIndex() {
	this_class = *(uint16_t *)zfilePtr;
	super_class = *(uint16_t *)(zfilePtr+2);
	zfilePtr += 4;
}

void ClassFile::readInterfaces() {
	interfaces_count = *(uint16_t *)zfilePtr;
	zfilePtr += 2;
	interfaces.add(this);
}

void ClassFile::readFields() {
	fields_count = *(uint16_t *)zfilePtr;
	zfilePtr += 2;
	fields.add(this);
}

void ClassFile::readMethods() {
	methods_count = *(uint16_t *)zfilePtr;
	zfilePtr += 2;
	methods.add(this);
}

void ClassFile::readAttributes() {
	attributes_count = *(uint16_t *)zfilePtr;
	zfilePtr += 2;
	attributes.add(this);
}


/**
 * Return pointer to class file
 */
uint8_t *ClassFile::getFilePtr() {
	return zfilePtr;
}

/**
 * Set the class file pointer
 */
void ClassFile::setFilePtr(uint8_t *ptr) {
	zfilePtr = ptr;
}
