#ifndef BEAGLE_BASE_H
#define BEAGLE_BASE_H


#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>


struct TypeInfo
{
	struct TypeInfo *base; // pointer to static data of base class/struct (NULL = no base)
	size_t staticSize;
	size_t dynamicSize;
	const char *name;
};


typedef struct
{
   void *base__;
   struct TypeInfo typeInfo__;
} static_string_;

typedef struct
{
	static_string_ *type__;
	uint32_t length;
	const char *content;
} dynamic_string_;

static static_string_ type_string_ = 
{
   .typeInfo__.base = NULL,
   .typeInfo__.staticSize = sizeof(static_string_),
   .typeInfo__.dynamicSize = sizeof(dynamic_string_),
   .typeInfo__.name = "string",
   .base__ = NULL
};

#endif // BEAGLE_BASE_H
