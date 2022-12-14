/**
 * <p>This package (introduced in Saxon 9.9) provides methods to manipulate XDM values using Java 8 streams.</p>
 * <p>A value in the XDM data model is represented by an instance of {@link net.sf.saxon.s9api.XdmValue}.
 * This is in general a sequence; the members of the sequence are instances of {@link net.sf.saxon.s9api.XdmItem}.
 * {@code XdmItem} is an abstract class; its concrete subclasses include {@link net.sf.saxon.s9api.XdmNode} for
 * nodes, {@link net.sf.saxon.s9api.XdmAtomicValue} for atomic values, {@link net.sf.saxon.s9api.XdmFunctionItem}
 * for function items, {@link net.sf.saxon.s9api.XdmMap} for maps, and {@link net.sf.saxon.s9api.XdmArray}
 * for arrays.</p>
 * <p>Given an <code>XdmNode</code> <code>N</code>, it is possible to select other nodes using an expression
 * such as <code>N.select(child("author")).asNode()</code>. The way this works is as follows:</p>
 * <ul>
 * <li>The <code>select()</code> method (which applies to any <code>XdmValue</code>, not only
 * an <code>XdmNode</code>) takes as its argument a {@link net.sf.saxon.s9api.streams.Step},
 * and returns as its result an {@link net.sf.saxon.s9api.streams.XdmStream}. A <code>Step</code>
 * is a function that takes an <code>XdmItem</code> as input, and returns a stream of items
 * as its result; the <code>select()</code> method combines these streams into a single stream
 * (in much the same way as the Java 8 <code>flatMap()</code> operation) and returns the result.</li>
 * <li><code>child("author")</code> invokes a static method in class {@link net.sf.saxon.s9api.streams.Steps},
 * which delivers a <code>Step</code> whose effect is to find the children of a supplied node that have
 * local name <code>"author"</code>, returning these as a Stream. The <code>Steps</code> class provides
 * a large collection of useful implementations of <code>Step</code>, including support for all the
 * XPath axes (parent, child, descendant, following-sibling and so on).</li>
 * <li>The class <code>XdmStream</code> (which will typically not be used explicitly) implements
 * the standard Java 8 {@code java.util.streams.Stream} class; it does so by wrapping a standard
 * Java 8 stream and delegating all standard methods to the wrapped stream. By subclassing the standard
 * <code>Stream</code> interface, however, it is able to supply additional methods appropriate to
 * streams of XDM items. For example, the <code>asNode()</code> example terminates the Stream pipeline
 * by converting the result of the stream to a single <code>XdmNode</code> value (an unchecked
 * exception occurs if the content of the stream is anything other than a single node).</li>
 * </ul>
 * <p>The power of the approach rests in the range of <code>Step</code> implementations available, and the way
 * these can be combined; and in the terminal operations to deliver the result of a stream in a useful way.
 * Although many standard implementations are available, these can be augmented with user-written methods;
 * since a {@code Step} is just a Java 8 {@code Function}, and an {@code XdmStream} is just a Java 8 {@code Stream},
 * all the standard Java 8 facilities for creating and combining functions and streams are available.</p>
 * <h2>Steps</h2>
 * <p>The steps that are available "off the shelf" from the {@link net.sf.saxon.s9api.streams.Steps}
 * class include the following:</p>
 * <ul>
 * <li><p>For each of the 13 XPath axes, taking <code>child</code> as an example, four <code>Step</code>
 * implementations are provided:</p>
 * <ul>
 * <li><code>child()</code> selects all the children of a node.</li>
 * <li><code>child("lname")</code> selects all the children of a node that have the local name "lname".</li>
 * <li><code>child("ns", "lname")</code> selects all the children of a node that have the namespace URI "ns"
 * and local name "lname".</li>
 * <li><code>child(predicate)</code> selects all the children of a node that satisfy the given predicate.
 * The predicate may be any Java 8 {@code java.util.functions.Predicate}, but the class
 * {@link net.sf.saxon.s9api.streams.Predicates} provides some off-the-shelf predicates that are
 * particularly designed for navigating XDM information.</li>
 * </ul>
 * </li>
 * <li><p>Two steps <code>S</code> and <code>T</code> may be combined into a single step using the <code>then</code>
 * method: for example <code>child("X").then(attribute("A"))</code>.</p></li>
 * <li><p>More generally, any sequence of steps may be combined using the <code>path()</code> function, for example
 * <code>path(child("table"), child("thead"), child("trow"), attribute("class"))</code></p></li>
 * <li><p>Where the steps in a path are the most commonly used variety, a path may be written using an abbreviated notation:
 * <code>path("table", "thead", "trow", "@class")</code> is equivalent to the previous example.</p></li>
 * <li><p>The results of two steps may also be concatenated: for example <code>child("author").cat(child("editor"))</code>
 * concatenates the results of the two steps into a single stream.</p></li>
 * <li><p>The <code>atomize()</code> step reduces nodes (and arrays) to atomic values, as defined in the XPath
 * specification.</p></li>
 * <li><p>If <code>S</code> is a <code>Step</code>, and <code>P</code> is a <code>Predicate</code>,
 * then <code>S.where(P)</code> is also a <code>Step</code>, which selects those elements selected by
 * <code>S</code> that satisfy <code>P</code>. For example <code>child().where(isElement())</code>
 * selects those children of the starting node that are elements. (But this particular example can be
 * written more easily as <code>child(isElement())</code>).</p></li>
 * </ul>
 * <h2>Predicates</h2>
 * <p>Predicates can be used either in the <code>where()</code> method to construct a new <code>Step</code>,
 * or they may be used in the standard Java 8 <code>filter()</code> method to filter the items in a stream.
 * Any predicate may be used in either context. The utility class {@link net.sf.saxon.s9api.streams.Predicates}
 * provides a range of predicates that are particularly suited to XDM navigation.</p>
 * <p>These predicates include:</p>
 * <ul>
 * <li>Predicates <code>isNode()</code>, <code>isAtomicValue()</code>, <code>isMap()</code>, <code>isArray()</code>
 * etc to test whether an <code>XdmItem</code> is a particular kind of item;</li>
 * <li>Predicates <code>isElement()</code>, <code>isAttribute()</code>, <code>isText()</code>
 * etc to test the kind of a node;</li>
 * <li>The predicate <code>hasType()</code> to test whether an item matches a specific item type: for example
 * <code>hasType(ItemType.XS_DATE_TIME)</code> tests whether it is an instance of <code>xs:dateTime</code>;</li>
 * <li>The predicate <code>eq("string")</code> tests whether the string value of the item is equal
 * to "string"</li>
 * <li>The predicate <code>eq(XdmAtomicValue)</code> performs a typed comparison, for example comparing two
 * values as numbers
 * </li>
 * <li>The predicate <code>matchesRegex("regex")</code> tests whether the string value of the item matches
 * the regular expression "regex"</li>
 * <li>If <code>S</code> is a <code>Step</code> and <code>P</code> is a <code>Predicate</code>, then
 * <code>some(S, P)</code> is a predicate that returns true if some item returned by <code>S</code>
 * satisfies <code>P</code>; similarly <code>every(S, P)</code> tests if all items returned by <code>S</code>
 * satisfy <code>P</code>. For example <code>some(attribute("id"), eq("A123"))</code> is true for an element
 * that has an <code>id</code> attribute equal to "A123". This particular condition can also be expressed
 * more concisely as <code>eq(attribute("id"), "A123")</code></li>
 * </ul>
 * <h2>Operations on XDM streams</h2>
 * <p>An <code>XdmStream</code> (as delivered by <code>XdmValue.select(step)</code>) is an implementation of
 * a Java 8 Stream, so all the standard methods on <code>Stream</code> are available: for example <code>filter</code>,
 * <code>map()</code>, <code>flatMap</code>, <code>reduce</code>, <code>collect</code>. Where appropriate, these
 * are specialized to return an <code>XdmStream</code> rather than a generic <code>Stream</code>.</p>
 * <p><code>XdmStream</code> provides some additional terminal operations designed to make it convenient to convert
 * the contents of the stream into usable form. These include:</p>
 * <ul>
 * <li><code>first()</code> - deliver the first item of the stream</li>
 * <li><code>last()</code> - deliver the last item of the stream</li>
 * <li><code>at()</code> - deliver the item at position N the stream</li>
 * <li><code>exists()</code> - return true if the stream is non-empty</li>
 * <li><code>subStream()</code> - deliver a stream containing a subsequence of the input stream</li>
 * <li><code>asXdmValue()</code> - deliver the contents as an <code>XdmValue</code></li>
 * <li><code>asList()</code> - deliver the contents as a <code>List&lt;XdmItem&gt;</code></li>
 * <li><code>asListOfNodes()</code> - deliver the contents as a <code>List&lt;XdmNode&gt;</code></li>
 * <li><code>asOptionalNode()</code> - deliver the contents as an <code>Optional&lt;XdmNode&gt;</code></li>
 * <li><code>asNode()</code> - deliver the contents as a single <code>XdmNode</code></li>
 * <li><code>asListOfAtomic()</code> - deliver the contents as a <code>List&lt;XdmAtomicValue&gt;</code></li>
 * <li><code>asOptionalAtomic()</code> - deliver the contents as an <code>Optional&lt;XdmAtomicValue&gt;</code></li>
 * <li><code>asAtomic()</code> - deliver the contents as a single <code>XdmAtomicValue</code></li>
 * <li><code>asOptionalString()</code> - deliver the contents as an <code>Optional&lt;String&gt;</code> by taking
 * the string value of each item</li>
 * <li><code>asString()</code> - deliver the contents as a single <code>String</code></li>
 * </ul>
 * <p>The choice of terminal operation determines the return type (for example <code>asOptionalNode()</code>
 * returns <code>Optional&lt;XdmNode&gt;)</code>, and also causes a run-time check that the value actually
 * conforms to these expectations. For example, if <code>asNode()</code> is used, then an unchecked exception
 * occurs if the sequence has a length other than 1 (one), or if its single item is not a node.</p>
 * <p>Other ways of generating an <code>XdmStream</code> include:</p>
 * <ul>
 * <li>As the result of an XPath expression, using the method <code>XPathSelector.stream()</code></li>
 * <li>As the result of an XQuery expression, using the method <code>XQueryEvaluator.stream()</code></li>
 * <li>As the result of <code>XdmSequenceIterator.stream()</code></li>
 * </ul>
 * <hr>
 */
package net.sf.saxon.s9api.streams;
