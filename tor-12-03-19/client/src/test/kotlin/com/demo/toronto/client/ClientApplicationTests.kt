package com.demo.toronto.client

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.lang.ClassCastException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@SpringBootTest
class ClientApplicationTests {

    private inline fun <reified T> reifiedInline(something: T): T {
        return when (something) {
            is Number -> 1
            is String -> "Uno"
            else -> something
        } as T
    }

    @Test
    fun reifiedFunctionCall() {
        Assertions
                .assertThat(reifiedInline(34242342))
                .isNotNull()
                .isInstanceOf(java.lang.Integer::class.java)

        Assertions
                .assertThat(reifiedInline("34242342"))
                .isNotNull()
                .isNotInstanceOf(java.lang.Integer::class.java)

        Assertions
                .assertThat(reifiedInline("34242342"))
                .isNotNull()
                .isInstanceOf(String::class.java)

    }

    private final inline fun <T> inlinePredicate(aSuffix: Pair<String, String>, body: () -> T): T =
            (aSuffix.first + body().toString() + aSuffix.second) as T

	private final fun <T> ordinary(functor: () -> T): T {
		return functor()
	}

    @Test
    fun `inline function tests`() {
        Assertions
                .assertThat(
                        inlinePredicate(Pair("A", "B")) {
                            "hi"
                        })
                .isNotNull()
				.isInstanceOf(String::class.java)
				.isEqualTo("AhiB")
    }

	@Test
	fun `inlined functions can respond for regular functions`() {
		assert(true)
		inlinePredicate(Pair("C","D")) {
				return
		}
		assert(false)
	}

	private final inline fun <T> crossPredicate(aSuffix: Pair<String, String>, crossinline body: (String) -> T): T =
			(aSuffix.first + body(aSuffix.second)) as T

	@Test
	fun `cross inlined functions`() {
			val t = crossPredicate(Pair("A", "B")) {
				"hi $it"
			}
		Assertions
				.assertThat(t)
				.isNotNull()
				.isInstanceOf(String::class.java)
				.isEqualTo("Ahi B")
	}

	open class TypeLiteral<T> {
		val type: Type
			get() = (javaClass.getGenericSuperclass() as ParameterizedType).getActualTypeArguments()[0]
	}

	private inline fun <reified T> typeLiteral(): TypeLiteral<T> = object : TypeLiteral<T>() {} // here T is replaced with the actual type
	@Test
	fun `type literals`() {
		Assertions
				.assertThatThrownBy {
					TypeLiteral<String>().type
				}
				.isInstanceOf(ClassCastException::class.java)

		Assertions.
				assertThat(typeLiteral<String>().type)
				.isNotNull
				.isEqualTo(String::class.java)
	}
}
