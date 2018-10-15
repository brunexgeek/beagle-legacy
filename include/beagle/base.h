#ifndef BEAGLE_BASE_H
#define BEAGLE_BASE_H


#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>


#if defined(WIN32) || defined(_WIN32)
    #define __WINDOWS__
#else
    #define __UNIX__
#endif


#if defined(__WINDOWS__) || defined(_MSC_VER)
    #define BGL_PRIVATE
    #define BGL_PUBLIC
    #ifdef __cplusplus
        #define BGL_LIB_EXPORT  extern "C" __declspec(dllexport)
    #else
        #define BGL_LIB_EXPORT  extern __declspec(dllexport)
    #endif
    #define BGL_LIB_IMPORT  extern __declspec(dllimport)
#else
    #define BGL_PRIVATE  __attribute__((visibility("hidden")))
    #define BGL_PUBLIC  __attribute__((visibility("default")))
    #ifdef __cplusplus
        #define BGL_LIB_EXPORT  extern "C" BGL_PUBLIC
    #else
        #define BGL_LIB_EXPORT  extern BGL_PUBLIC
    #endif
    #define BGL_LIB_IMPORT extern
#endif


#ifndef __WINDOWS__
    #define BGL_FUNCTION(type, name)   type __fastcall name
#else
    #define BGL_FUNCTION(type, name)   type name
#endif


#define BGL_NULL (void*)0
#ifndef BGL_TRUE
    #define BGL_TRUE (uint8_t)1
#endif
#ifndef BGL_FALSE
    #define BGL_FALSE (uint8_t)0
#endif

typedef uint8_t  beagle_uint8;
typedef uint16_t beagle_uint16;
typedef uint32_t beagle_uint32;
typedef uint64_t beagle_uint64;
typedef int8_t   beagle_int8;
typedef int16_t  beagle_int16;
typedef int32_t  beagle_int32;
typedef int64_t  beagle_int64;
typedef float    beagle_float32;
typedef double   beagle_float64;
typedef uint8_t  beagle_bool;


struct TypeInfo
{
	struct TypeInfo *base; // pointer to static data of base class/struct (NULL = no base)
	size_t staticSize;
	size_t dynamicSize;
	const char *name;
};

typedef struct beagle_frame
{
    struct beagle_frame* prev;
    const char *function;
    const char *fileName;
    uint32_t line;
    uint32_t depth;
    uint32_t size;
    uint8_t content[];
} beagle_frame;


/**
 * String class
 */
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

typedef const dynamic_string_ *beagle_string;

#endif // BEAGLE_BASE_H
