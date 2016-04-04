def answer(chunk, word):

    return chunk.replace(word,"",10)


def main():
    print(answer(chunk="goodgooogoogfogoood", word = "goo"))
    print(answer(chunk="lololololo", word = "lol"))

if __name__ == "__main__":
    main()