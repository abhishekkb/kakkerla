def answer(x):
    out = [list(y) for y in x]

    print(out)
    outrev=out

    for item in out:
        #print(item)
        itemrev = list(reversed(item))
        if itemrev not in outrev:
            outrev.append(itemrev)

    print(outrev)

    return len(outrev)


def main():
    x = ["foo", "bar", "", "rba","rab"] #["x", "y", "xy", "yy", "", "yx"] #
    print (answer(x))

if __name__ == "__main__":
    main()
