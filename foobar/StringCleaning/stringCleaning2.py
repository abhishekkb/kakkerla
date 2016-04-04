def poss(chunk, word):
    if chunk == word:
        return chunk
    if not chunk:
        return chunk
    if chunk.find(word) == -1:
        return chunk
    return poss(chunk.replace(word, ''), word)

def occur(chunk, word):
    lengthAtOccurances = [i for i in range(len(chunk)) if chunk.startswith(word, i)]
    return lengthAtOccurances

def answer(chunk, word):
    out = []
    out.append(poss(chunk, word))
    out.append(poss(chunk[::-1], word[::-1])[::-1])
    occurs = occur(chunk, word)
    for i in occurs:
        l = list(chunk)
        del(l[i:i+len(word)])
        chunks = "".join(l)
        out.append(poss(chunks, word))
        while chunks.find(word) != -1:
            chunks = chunks.replace(word, '', 1)
        out.append(poss(chunks, word))
    return sorted(out)[0]

def main():
    print(answer(chunk="goodgooogoogfogoood", word = "goo"))
    print(answer(chunk="lololololo", word = "lol"))

if __name__ == "__main__":
    main()