def answer(x):
    out = [list(y.lower()) for y in x]
    notDupes=[]

    while len(out) != 0:
        item = out.pop()
        notDupes.append(item)
        if item in out:
            out = list(filter(item.__ne__, out))

        itemrev=list(reversed(item))
        if itemrev in out:
            out = list(filter(itemrev.__ne__, out))

        #if item in out:
        #    out = (x for x in out if ((x not in out) or (list(reversed(x)) not in out)))

    return len(notDupes)

def main():
    x = ["foo", "bar", "", "rba","rab"] #["x", "y", "xy", "yy", "", "yx"] #
    q = answer(x)
    print(q)
if __name__ == "__main__":
    main()
